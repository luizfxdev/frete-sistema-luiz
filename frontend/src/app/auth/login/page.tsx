"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";
import { post } from "@/core/api/publicHttpClient";
import { useAuthStore, type AuthUser } from "@/core/auth/authStore";

interface LoginResponse {
  id: number;
  nome: string;
  email: string;
  role: AuthUser["role"];
  token: string;
}

export default function LoginPage() {
  const router = useRouter();
  const setUsuario = useAuthStore((s) => s.setUsuario);
  const [form, setForm] = useState({ email: "", senha: "" });
  const [showSenha, setShowSenha] = useState(false);
  const [lembrar, setLembrar] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      const res = await post<LoginResponse>("/auth/login", form);
      setUsuario({ id: res.id, nome: res.nome, email: res.email, role: res.role }, res.token);
      router.push(res.role === "MOTORISTA" ? "/motorista/perfil" : "/dashboard");
    } catch {
      setError("E-mail ou senha inválidos.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-overlay" />

      <header className="auth-page-header">
        <Link
          href="/"
          className="auth-page-link"
        >
          <i className="bi bi-arrow-left" />
          Página Inicial
        </Link>
      </header>

      <div className="auth-card">
        <div className="auth-logo">
          Next<em>Log</em>
        </div>

        <h2 className="auth-title">Bem-vindo de volta</h2>

        <form onSubmit={handleSubmit} className="auth-form">
          <div className="auth-input-box">
            <input
              type="email"
              placeholder="E-mail"
              autoComplete="email"
              value={form.email}
              onChange={(e) => setForm((f) => ({ ...f, email: e.target.value }))}
              required
            />
            <i className="ri-user-fill" />
          </div>

          <div className="auth-input-box">
            <input
              id="senha"
              type={showSenha ? "text" : "password"}
              placeholder={showSenha ? "sua senha aqui" : "••••••••"}
              autoComplete="current-password"
              value={form.senha}
              onChange={(e) => setForm((f) => ({ ...f, senha: e.target.value }))}
              required
            />
            <i
              className={showSenha ? "bi bi-eye" : "bi bi-eye-slash"}
              onClick={() => setShowSenha((v) => !v)}
              style={{ cursor: "pointer" }}
            />
          </div>

          <div className="auth-remember">
            <label>
              <input
                type="checkbox"
                checked={lembrar}
                onChange={(e) => setLembrar(e.target.checked)}
              />
              Lembrar-me
            </label>
            <Link href="/auth/esqueci-senha">Esqueci a senha</Link>
          </div>

          {error && <p className="auth-error">{error}</p>}

          <button type="submit" className="auth-btn" disabled={loading}>
            <span>{loading ? "Entrando..." : "Entrar"}</span>
          </button>
        </form>
      </div>

      <style jsx>{`
        @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap');

        * {
          margin: 0;
          padding: 0;
          box-sizing: border-box;
        }

        html, body {
          width: 100%;
          height: 100%;
        }

        .auth-page {
          min-height: 100vh;
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          background-image: url('/assets/backgrounds/loginbackground.png');
          background-size: cover;
          background-position: center;
          background-attachment: fixed;
          background-color: #050f1a;
          font-family: 'Poppins', sans-serif;
          color: #fff;
          padding: 24px;
          width: 100%;
          min-width: 100vw;
          position: relative;
        }

        .auth-overlay {
          position: absolute;
          inset: 0;
          background-color: rgba(5, 15, 26, 0.65);
          z-index: 0;
        }

        .auth-page-header {
          position: absolute;
          top: 24px;
          right: 24px;
          z-index: 20;
        }

        .auth-page-link {
          display: flex;
          align-items: center;
          gap: 6px;
          font-size: 14px;
          font-weight: 500;
          color: rgba(255, 255, 255, 0.6);
          text-decoration: none;
          transition: color 0.15s;
        }

        .auth-page-link:hover {
          color: #fff;
        }

        .auth-page-link i {
          font-size: 16px;
        }

        .auth-card {
          width: 100%;
          max-width: 500px;
          background: transparent;
          border: 2px solid rgba(255, 255, 255, 0.2);
          backdrop-filter: blur(13px);
          -webkit-backdrop-filter: blur(13px);
          border-radius: 12px;
          padding: 50px 45px;
          box-shadow: 0 8px 32px rgba(0, 0, 0, 0.25);
          position: relative;
          z-index: 10;
        }

        .auth-logo {
          font-size: 36px;
          font-weight: 800;
          letter-spacing: -0.03em;
          text-align: center;
          margin-bottom: 12px;
          color: #fff;
          font-family: 'Avenir Next', -apple-system, sans-serif;
        }

        .auth-logo em {
          font-style: italic;
          color: rgba(255, 255, 255, 0.7);
          font-family: 'Libre Baskerville', Georgia, serif;
          font-weight: 400;
        }

        .auth-title {
          font-size: 16px;
          font-weight: 400;
          text-align: center;
          color: rgba(255, 255, 255, 0.6);
          margin: 0 0 35px 0;
        }

        .auth-form {
          display: flex;
          flex-direction: column;
          gap: 0;
        }

        .auth-input-box {
          position: relative;
          width: 100%;
          height: 60px;
          margin-bottom: 28px;
        }

        .auth-input-box input {
          background: transparent;
          width: 100%;
          height: 100%;
          border: 2px solid rgba(255, 255, 255, 0.2);
          border-radius: 30px;
          padding: 0 50px 0 22px;
          font-size: 15px;
          font-family: 'Poppins', sans-serif;
          color: #fff;
          transition: border-color 0.2s;
        }

        .auth-input-box input::placeholder {
          color: rgba(255, 255, 255, 0.6);
        }

        .auth-input-box input:focus {
          border-color: rgba(255, 255, 255, 0.5);
          outline: none;
        }

        .auth-input-box i {
          position: absolute;
          top: 50%;
          right: 18px;
          transform: translateY(-50%);
          font-size: 20px;
          color: rgba(255, 255, 255, 0.7);
          transition: all 0.2s ease;
          cursor: pointer;
        }

        .auth-input-box i:hover {
          color: rgba(255, 255, 255, 1);
          transform: translateY(-50%) scale(1.1);
        }

        .auth-remember {
          display: flex;
          align-items: center;
          justify-content: space-between;
          margin-bottom: 24px;
          font-size: 14px;
        }

        .auth-remember label {
          display: flex;
          align-items: center;
          gap: 8px;
          cursor: pointer;
          color: rgba(255, 255, 255, 0.8);
        }

        .auth-remember label input[type="checkbox"] {
          width: 17px;
          height: 17px;
          accent-color: #fff;
          cursor: pointer;
        }

        .auth-remember a {
          color: rgba(255, 255, 255, 0.8);
          font-size: 14px;
          text-decoration: none;
          transition: color 0.15s;
        }

        .auth-remember a:hover {
          color: #fff;
          text-decoration: underline;
        }

        .auth-error {
          background: rgba(239, 68, 68, 0.15);
          border: 1px solid rgba(239, 68, 68, 0.3);
          border-radius: 8px;
          padding: 12px 16px;
          font-size: 13px;
          color: #fca5a5;
          margin-bottom: 20px;
          text-align: center;
        }

        .auth-btn {
          width: 100%;
          height: 52px;
          background: #005eff;
          color: #fff;
          border: none;
          border-radius: 30px;
          font-size: 16px;
          font-weight: 600;
          font-family: 'Poppins', sans-serif;
          cursor: pointer;
          position: relative;
          overflow: hidden;
          transition: all 0.2s;
          box-shadow: 0 0 10px rgba(0, 94, 255, 0.3);
        }

        .auth-btn::before {
          content: '';
          position: absolute;
          top: 50%;
          left: 50%;
          width: 0;
          height: 0;
          border-radius: 50%;
          background: rgba(255, 255, 255, 0.15);
          transform: translate(-50%, -50%);
          transition: width 0.5s, height 0.5s;
        }

        .auth-btn:hover::before {
          width: 350px;
          height: 350px;
        }

        .auth-btn:hover {
          box-shadow: 0 4px 20px rgba(0, 94, 255, 0.45);
          transform: translateY(-1px);
        }

        .auth-btn:active {
          transform: scale(0.98);
        }

        .auth-btn:disabled {
          opacity: 0.6;
          cursor: not-allowed;
          transform: none;
        }

        .auth-btn span {
          position: relative;
          z-index: 1;
        }

        @media (max-width: 768px) {
          .auth-page-header {
            top: 16px;
            right: 16px;
          }

          .auth-page-link {
            font-size: 13px;
          }

          .auth-card {
            max-width: 100%;
            padding: 45px 35px;
          }

          .auth-logo {
            font-size: 32px;
          }

          .auth-title {
            font-size: 15px;
            margin-bottom: 30px;
          }

          .auth-input-box {
            height: 55px;
            margin-bottom: 24px;
          }
        }
      `}</style>
    </div>
  );
}
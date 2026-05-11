"use client";

import { useState } from "react";
import Link from "next/link";
import { Navbar } from "@/features/landing/components/Navbar";
import { Footer } from "@/features/landing/components/Footer";
import { post } from "@/core/api/publicHttpClient";

type Step = "form" | "enviado";

export default function EsqueciSenhaPage() {
  const [email, setEmail] = useState("");
  const [step, setStep] = useState<Step>("form");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      await post("/auth/esqueci-senha", { email });
      setStep("enviado");
    } catch {
      setError("Não foi possível enviar o e-mail. Tente novamente.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <Navbar />
      <div className="auth-page">
        <div className="auth-card">
          <div className="auth-logo">
            Next<em>Log</em>
          </div>

          {step === "form" ? (
            <>
              <h2 className="auth-title">Recuperar senha</h2>
              <p className="auth-desc">
                Informe seu e-mail e enviaremos um link para redefinir sua senha.
              </p>

              <form onSubmit={handleSubmit} className="auth-form">
                <div className="auth-input-box">
                  <input
                    type="email"
                    placeholder="E-mail cadastrado"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                  />
                  <i className="ri-mail-fill" />
                </div>

                {error && <p className="auth-error">{error}</p>}

                <button type="submit" className="auth-btn" disabled={loading}>
                  <span>{loading ? "Enviando..." : "Enviar link"}</span>
                </button>
              </form>
            </>
          ) : (
            <div className="auth-success">
              <i className="ri-checkbox-circle-line auth-success-icon" />
              <h2 className="auth-title">E-mail enviado!</h2>
              <p className="auth-desc">
                Verifique sua caixa de entrada e siga as instruções para redefinir sua senha.
              </p>
            </div>
          )}

          <div className="auth-back">
            <Link href="/auth/login">
              <i className="ri-arrow-left-line" /> Voltar ao login
            </Link>
          </div>
        </div>

        <style jsx>{`
          @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap');

          .auth-page {
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            background-image: url('/assets/img/background.png');
            background-size: cover;
            background-position: center;
            font-family: 'Poppins', sans-serif;
            color: #fff;
            padding: 24px;
          }

          .auth-card {
            width: 100%;
            max-width: 400px;
            background: transparent;
            border: 2px solid rgba(255, 255, 255, 0.2);
            backdrop-filter: blur(13px);
            -webkit-backdrop-filter: blur(13px);
            border-radius: 12px;
            padding: 40px 35px;
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.25);
          }

          .auth-logo {
            font-size: 28px;
            font-weight: 800;
            letter-spacing: -0.03em;
            text-align: center;
            margin-bottom: 4px;
            color: #fff;
            font-family: 'Avenir Next', -apple-system, sans-serif;
          }
          .auth-logo em {
            font-style: italic;
            color: rgba(255,255,255,0.7);
            font-family: 'Libre Baskerville', Georgia, serif;
            font-weight: 400;
          }

          .auth-title {
            font-size: 22px;
            font-weight: 700;
            text-align: center;
            color: #fff;
            margin-bottom: 8px;
            margin-top: 20px;
          }

          .auth-desc {
            font-size: 13px;
            color: rgba(255, 255, 255, 0.55);
            text-align: center;
            line-height: 1.6;
            margin-bottom: 24px;
          }

          .auth-form { display: flex; flex-direction: column; }

          .auth-input-box {
            position: relative;
            width: 100%;
            height: 55px;
            margin-bottom: 20px;
          }

          .auth-input-box input {
            background: transparent;
            width: 100%;
            height: 100%;
            border: 2px solid rgba(255, 255, 255, 0.2);
            border-radius: 30px;
            padding: 0 45px 0 20px;
            font-size: 15px;
            font-family: 'Poppins', sans-serif;
            color: #fff;
            transition: border-color 0.2s;
          }

          .auth-input-box input::placeholder { color: rgba(255, 255, 255, 0.6); }
          .auth-input-box input:focus { border-color: rgba(255, 255, 255, 0.5); outline: none; }

          .auth-input-box :global(i) {
            position: absolute;
            top: 50%;
            right: 18px;
            transform: translateY(-50%);
            font-size: 18px;
            color: rgba(255, 255, 255, 0.7);
          }

          .auth-error {
            background: rgba(239, 68, 68, 0.15);
            border: 1px solid rgba(239, 68, 68, 0.3);
            border-radius: 8px;
            padding: 10px 14px;
            font-size: 13px;
            color: #fca5a5;
            margin-bottom: 16px;
            text-align: center;
          }

          .auth-btn {
            width: 100%;
            height: 48px;
            background: #fff;
            color: #0a2540;
            border: none;
            border-radius: 30px;
            font-size: 15px;
            font-weight: 600;
            font-family: 'Poppins', sans-serif;
            cursor: pointer;
            position: relative;
            overflow: hidden;
            transition: all 0.2s;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.15);
          }

          .auth-btn::before {
            content: '';
            position: absolute;
            top: 50%;
            left: 50%;
            width: 0;
            height: 0;
            border-radius: 50%;
            background: rgba(0, 94, 255, 0.15);
            transform: translate(-50%, -50%);
            transition: width 0.5s, height 0.5s;
          }

          .auth-btn:hover::before { width: 350px; height: 350px; }
          .auth-btn:hover { box-shadow: 0 4px 20px rgba(0,0,0,0.2); transform: translateY(-1px); }
          .auth-btn:active { transform: scale(0.98); }
          .auth-btn:disabled { opacity: 0.6; cursor: not-allowed; transform: none; }
          .auth-btn span { position: relative; z-index: 1; }

          .auth-success {
            display: flex;
            flex-direction: column;
            align-items: center;
            text-align: center;
          }

          .auth-success-icon {
            font-size: 52px;
            color: #22c55e;
            margin-bottom: 4px;
          }

          .auth-back { text-align: center; margin-top: 24px; }

          .auth-back :global(a) {
            display: inline-flex;
            align-items: center;
            gap: 6px;
            font-size: 13px;
            color: rgba(255, 255, 255, 0.55);
            transition: color 0.15s;
          }

          .auth-back :global(a:hover) { color: #fff; }
        `}</style>
      </div>
      <Footer />
    </>
  );
}
"use client";

import { useState } from "react";
import { IndicadoresCard } from "@/features/dashboard/components/IndicadoresCard";
import { MenuHamburger } from "@/features/dashboard/components/MenuHamburger";
import { useDashboard } from "@/features/dashboard/hooks/useDashboard";
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from "recharts";

export default function DashboardPage() {
  const { indicadores, loading, error } = useDashboard();
  const [menuOpen, setMenuOpen] = useState(false);

  const chartData = [
    { day: "Seg", entregas: 12, prazo: 10, atraso: 2 },
    { day: "Ter", entregas: 19, prazo: 15, atraso: 4 },
    { day: "Qua", entregas: 8, prazo: 6, atraso: 2 },
    { day: "Qui", entregas: 22, prazo: 20, atraso: 2 },
    { day: "Sex", entregas: 18, prazo: 16, atraso: 2 },
    { day: "Sab", entregas: 15, prazo: 14, atraso: 1 },
  ];

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen" style={{ backgroundColor: "#f5f5f5" }}>
        <div className="text-center">
          <div className="inline-block animate-spin">
            <i className="bi bi-arrow-repeat text-4xl text-blue-600"></i>
          </div>
          <p className="mt-4 text-gray-600" style={{ fontFamily: "var(--font-primary)" }}>
            Carregando indicadores...
          </p>
        </div>
      </div>
    );
  }

  if (error || !indicadores) {
    return (
      <div className="flex items-center justify-center min-h-screen" style={{ backgroundColor: "#f5f5f5" }}>
        <div className="text-center">
          <i className="bi bi-exclamation-circle text-5xl text-red-500"></i>
          <p className="mt-4 text-gray-900" style={{ fontFamily: "var(--font-primary)" }}>
            {error || "Erro ao carregar dados"}
          </p>
        </div>
      </div>
    );
  }

  return (
    <div style={{ backgroundColor: "#f5f5f5", minHeight: "100vh", width: "100%" }}>
      <style>{`
        @keyframes fadeIn {
          from { opacity: 0; transform: translateY(10px); }
          to { opacity: 1; transform: translateY(0); }
        }

        .animate-fadeIn { animation: fadeIn 0.5s ease-out forwards; }

        input[type="checkbox"]#burger-toggle {
          position: absolute;
          appearance: none;
          opacity: 0;
        }

        input[type="checkbox"]#burger-toggle:checked ~ .menu {
          opacity: 1;
          visibility: visible;
        }

        input[type="checkbox"]#burger-toggle:checked ~ .menu .menu-nav-link span div,
        input[type="checkbox"]#burger-toggle:checked ~ .menu img,
        input[type="checkbox"]#burger-toggle:checked ~ .menu .title p {
          transform: translateY(0);
          transition: 1.2s 0.1s cubic-bezier(0.35, 0, 0.07, 1);
        }

        input[type="checkbox"]#burger-toggle:checked ~ .menu .image-link:nth-child(1) img { transition-delay: 0.1s + 0.08s * 1; }
        input[type="checkbox"]#burger-toggle:checked ~ .menu .image-link:nth-child(2) img { transition-delay: 0.1s + 0.08s * 2; }
        input[type="checkbox"]#burger-toggle:checked ~ .menu .image-link:nth-child(3) img { transition-delay: 0.1s + 0.08s * 3; }

        input[type="checkbox"]#burger-toggle:checked ~ .burger-menu .line:nth-child(1) { transform: translateY(calc(4em / 5)) rotate(45deg); }
        input[type="checkbox"]#burger-toggle:checked ~ .burger-menu .line:nth-child(2) { transform: scaleX(0); }
        input[type="checkbox"]#burger-toggle:checked ~ .burger-menu .line:nth-child(3) { transform: translateY(calc(4em / -5)) rotate(-45deg); }
        input[type="checkbox"]#burger-toggle:checked ~ .burger-menu .line::after { transform: translateX(0); }

        .burger-menu {
          position: fixed;
          top: 2.5rem;
          left: 2rem;
          z-index: 100;
          display: block;
          width: 4em;
          height: 4em;
          outline: none;
          cursor: pointer;
        }

        .burger-menu .line {
          position: absolute;
          left: 25%;
          width: 50%;
          height: 3px;
          background: hsla(210, 29%, 24%, 0.3);
          border-radius: 10px;
          overflow: hidden;
          transition: 0.5s;
        }

        .burger-menu .line:nth-child(1) { top: 30%; }
        .burger-menu .line:nth-child(2) { top: 50%; }
        .burger-menu .line:nth-child(3) { top: 70%; }

        .burger-menu .line::after {
          position: absolute;
          content: "";
          top: 0;
          left: 0;
          width: 100%;
          height: 100%;
          background: #292929;
          transform: translateX(-100%);
          transition: 0.25s;
        }

        .burger-menu .line:nth-child(2)::after { transition-delay: 0.1s; }
        .burger-menu .line:nth-child(3)::after { transition-delay: 0.2s; }
        .burger-menu:hover .line::after { transform: translateX(0); }

        .menu {
          position: fixed;
          top: 0;
          left: 0;
          width: 100%;
          height: 100%;
          display: flex;
          justify-content: center;
          align-items: center;
          background: #f5f5f5;
          opacity: 0;
          overflow-x: hidden;
          visibility: hidden;
          transition: 0.3s;
          z-index: 99;
        }

        .menu-nav {
          display: flex;
          flex-wrap: wrap;
          margin: 0;
          padding: 0;
          text-align: center;
          list-style-type: none;
          gap: 2rem;
          justify-content: center;
        }

        @media screen and (max-width: 750px) {
          .menu-nav { flex-direction: column; }
        }

        .menu-nav-item { flex: 1; min-width: auto; }

        .menu-nav-link {
          position: relative;
          display: inline-flex;
          font-size: 2rem;
          color: #292929;
          text-decoration: none;
          font-family: 'Avenir Next', sans-serif;
          background: none;
          border: none;
          padding: 0;
          cursor: pointer;
        }

        .menu-nav-link span { overflow: hidden; }
        .menu-nav-link span div { transform: translateY(102%); }

        .menu-nav-link::after {
          position: absolute;
          content: "";
          top: 100%;
          left: 0;
          width: 100%;
          height: 3px;
          background: #292929;
          transform: scaleX(0);
          transform-origin: right;
          transition: transform 0.5s;
        }

        .menu-nav-link:hover::after {
          transform: scaleX(1);
          transform-origin: left;
        }

        .gallery { margin-top: 60px; text-align: center; }

        .title {
          font-size: 24px;
          color: #292929;
          overflow: hidden;
          font-family: 'Avenir Next', sans-serif;
        }

        .title p {
          font-size: 12px;
          letter-spacing: 2px;
          text-transform: uppercase;
          transform: translateY(102%);
          margin: 0;
        }

        .images {
          margin-top: 36px;
          display: flex;
          flex-wrap: wrap;
          justify-content: center;
          gap: 1rem;
        }

        .image-link { width: 15vw; overflow: hidden; }

        @media screen and (max-width: 750px) {
          .image-link { width: 40vw; }
        }

        .image {
          position: relative;
          transition: 0.6s;
          width: 100%;
          height: 250px;
        }

        .image::before {
          position: absolute;
          content: attr(data-label);
          top: 0;
          left: 0;
          z-index: 1;
          display: flex;
          justify-content: center;
          align-items: center;
          width: 100%;
          height: 100%;
          color: white;
          background: rgba(0, 0, 0, 0.6);
          opacity: 0;
          transition: 0.4s;
          font-family: 'Avenir Next', sans-serif;
        }

        .image-link:hover .image { transform: scale(1.2); }
        .image-link:hover .image::before { opacity: 1; }

        .image-link img {
          height: 250px;
          width: 100%;
          transform: translateY(102%);
          object-fit: cover;
        }

        .user-section {
          margin-top: 80px;
          display: flex;
          flex-direction: column;
          align-items: center;
          gap: 1rem;
        }

        .user-info {
          display: flex;
          align-items: center;
          gap: 1rem;
          padding: 1rem;
          background: rgba(41, 41, 41, 0.1);
          border-radius: 12px;
        }

        .user-avatar {
          width: 40px;
          height: 40px;
          border-radius: 50%;
          background: #292929;
          color: white;
          display: flex;
          align-items: center;
          justify-content: center;
          font-weight: bold;
          font-size: 18px;
          font-family: 'Avenir Next', sans-serif;
        }

        .user-details { display: flex; flex-direction: column; gap: 2px; }

        .user-name {
          margin: 0;
          font-size: 14px;
          font-weight: bold;
          color: #292929;
          font-family: 'Avenir Next', sans-serif;
        }

        .user-role {
          margin: 0;
          font-size: 11px;
          color: #666;
          text-transform: uppercase;
          letter-spacing: 1px;
          font-family: 'Avenir Next', sans-serif;
        }

        .logout-btn {
          display: flex;
          align-items: center;
          gap: 0.5rem;
          padding: 0.75rem 1.5rem;
          background: #292929;
          color: white;
          border: none;
          border-radius: 8px;
          font-family: 'Avenir Next', sans-serif;
          font-size: 14px;
          cursor: pointer;
          transition: opacity 0.3s;
        }

        .logout-btn:hover { opacity: 0.8; }
        .logout-btn i { font-size: 16px; }
      `}</style>

      <MenuHamburger isOpen={menuOpen} onToggle={setMenuOpen} />

      <main
        style={{
          opacity: menuOpen ? 0 : 1,
          pointerEvents: menuOpen ? "none" : "auto",
          transition: "opacity 0.3s",
          width: "100%",
        }}
      >
        <div style={{ maxWidth: "1400px", width: "100%", margin: "0 auto", padding: "5rem 4rem 4rem" }}>
          <div className="mb-12">
            <h1 className="text-3xl font-bold text-gray-900 mb-2" style={{ fontFamily: "var(--font-primary)" }}>
              Bem-vindo ao Dashboard
            </h1>
            <p className="text-gray-500 text-sm" style={{ fontFamily: "var(--font-primary)" }}>
              {new Date().toLocaleDateString("pt-BR", {
                weekday: "long",
                day: "numeric",
                month: "long",
                year: "numeric",
              })}
            </p>
          </div>

          {!menuOpen && <IndicadoresCard data={indicadores} />}

          <div className="mt-20 mb-20">
            <div className="mb-8">
              <h2 className="text-2xl font-bold text-gray-900" style={{ fontFamily: "var(--font-primary)" }}>
                Evolução de Entregas
              </h2>
              <p className="text-sm text-gray-500 mt-1" style={{ fontFamily: "var(--font-primary)" }}>
                Acompanhamento semanal de desempenho
              </p>
            </div>

            <div className="bg-white rounded-2xl p-6 shadow-sm" style={{ height: "400px" }}>
              <ResponsiveContainer width="100%" height="100%">
                <LineChart data={chartData}>
                  <CartesianGrid strokeDasharray="3 3" stroke="#e5e7eb" />
                  <XAxis dataKey="day" stroke="#9ca3af" />
                  <YAxis stroke="#9ca3af" />
                  <Tooltip contentStyle={{ backgroundColor: "#fff", border: "1px solid #e5e7eb", borderRadius: "8px" }} />
                  <Legend wrapperStyle={{ fontFamily: "var(--font-primary)" }} />
                  <Line type="monotone" dataKey="entregas" stroke="#005eff" name="Total de Entregas" strokeWidth={2} />
                  <Line type="monotone" dataKey="prazo" stroke="#22c55e" name="No Prazo" strokeWidth={2} />
                  <Line type="monotone" dataKey="atraso" stroke="#ef4444" name="Com Atraso" strokeWidth={2} />
                </LineChart>
              </ResponsiveContainer>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
}
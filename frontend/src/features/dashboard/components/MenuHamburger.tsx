"use client";

import React, { useState } from "react";
import { useRouter } from "next/navigation";
import { useAuthStore } from "@/core/auth/authStore";

interface MenuHamburgerProps {
  isOpen: boolean;
  onToggle: (open: boolean) => void;
}

export function MenuHamburger({ isOpen, onToggle }: MenuHamburgerProps) {
  const router = useRouter();
  const usuario = useAuthStore((s) => s.usuario);
  const logout = useAuthStore((s) => s.logout);

  const handleLogout = () => {
    logout();
    router.push("/auth/login");
  };

  return (
    <>
      <input
        type="checkbox"
        id="burger-toggle"
        checked={isOpen}
        onChange={(e) => onToggle(e.target.checked)}
      />
      <label htmlFor="burger-toggle" className="burger-menu">
        <div className="line"></div>
        <div className="line"></div>
        <div className="line"></div>
      </label>

      <div className="menu">
        <div className="flex flex-col items-center justify-center gap-8">
          <ul className="menu-nav">
            <li className="menu-nav-item">
              <button
                onClick={() => {
                  router.push("/dashboard");
                  onToggle(false);
                }}
                className="menu-nav-link"
              >
                <span>
                  <div>Início</div>
                </span>
              </button>
            </li>
            <li className="menu-nav-item">
              <a href="http://localhost:8080/nextlog/fretes" className="menu-nav-link" target="_blank" rel="noopener noreferrer">
                <span>
                  <div>Fretes</div>
                </span>
              </a>
            </li>
            <li className="menu-nav-item">
              <a href="http://localhost:8080/nextlog/clientes" className="menu-nav-link" target="_blank" rel="noopener noreferrer">
                <span>
                  <div>Clientes</div>
                </span>
              </a>
            </li>
            <li className="menu-nav-item">
              <a href="http://localhost:8080/nextlog/motoristas" className="menu-nav-link" target="_blank" rel="noopener noreferrer">
                <span>
                  <div>Motoristas</div>
                </span>
              </a>
            </li>
            <li className="menu-nav-item">
              <a href="http://localhost:8080/nextlog/veiculos" className="menu-nav-link" target="_blank" rel="noopener noreferrer">
                <span>
                  <div>Veículos</div>
                </span>
              </a>
            </li>
          </ul>

          <div className="gallery">
            <div className="title">
              <p>Visão Geral</p>
            </div>
            <div className="images">
              <button
                onClick={() => {
                  router.push("/dashboard");
                  onToggle(false);
                }}
                className="image-link"
                style={{ background: "none", border: "none", padding: 0, cursor: "pointer" }}
              >
                <div className="image" data-label="Status de Entregas">
                  <img src="/assets/backgrounds/back1.png" alt="Status de Entregas" style={{ objectFit: "contain", objectPosition: "center" }} />
                </div>
              </button>
              <button
                onClick={() => {
                  router.push("/dashboard/performance");
                  onToggle(false);
                }}
                className="image-link"
                style={{ background: "none", border: "none", padding: 0, cursor: "pointer" }}
              >
                <div className="image" data-label="Performance">
                  <img src="/assets/backgrounds/back2.png" alt="Performance" style={{ objectFit: "contain", objectPosition: "center" }} />
                </div>
              </button>
              <button
                onClick={() => {
                  router.push("/dashboard/manutencao");
                  onToggle(false);
                }}
                className="image-link"
                style={{ background: "none", border: "none", padding: 0, cursor: "pointer" }}
              >
                <div className="image" data-label="Manutenção">
                  <img src="/assets/backgrounds/back3.png" alt="Manutenção" style={{ objectFit: "contain", objectPosition: "center" }} />
                </div>
              </button>
            </div>
          </div>

          <div className="user-section">
            {usuario && (
              <div className="user-info">
                <div className="user-avatar">
                  {usuario.nome.charAt(0).toUpperCase()}
                </div>
                <div className="user-details">
                  <p className="user-name">{usuario.nome}</p>
                  <p className="user-role">{usuario.role}</p>
                </div>
              </div>
            )}
            <button
              onClick={handleLogout}
              className="logout-btn"
            >
              <i className="bi bi-box-arrow-left"></i>
              <span>Sair</span>
            </button>
          </div>
        </div>
      </div>
    </>
  );
}
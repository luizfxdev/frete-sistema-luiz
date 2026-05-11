"use client";
import { useAuthStore } from "./authStore";

export function useAuth() {
  const { usuario, token, setUsuario, logout } = useAuthStore();
  return { usuario, token, isAuthenticated: !!token, setUsuario, logout };
}
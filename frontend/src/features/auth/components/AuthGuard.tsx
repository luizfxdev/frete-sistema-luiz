"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { useAuthStore } from "@/core/auth/authStore";

interface AuthGuardProps {
  children: React.ReactNode;
  requiredRole?: ("ADMIN" | "GESTOR" | "MOTORISTA")[];
}

export function AuthGuard({ children, requiredRole }: AuthGuardProps) {
  const router = useRouter();
  const { usuario, token, isLoading } = useAuthStore();
  const [mounted, setMounted] = useState(false);

  useEffect(() => {
    setMounted(true);
  }, []);

  useEffect(() => {
    if (!mounted || isLoading) return;

    if (!token || !usuario) {
      router.replace("/auth/login");
      return;
    }

    if (requiredRole && !requiredRole.includes(usuario.role)) {
      router.replace("/dashboard");
    }
  }, [mounted, isLoading, token, usuario, requiredRole, router]);

  if (!mounted || isLoading) {
    return (
      <div className="flex items-center justify-center min-h-screen bg-gray-50">
        <div className="text-center">
          <div className="inline-block animate-spin">
            <i className="bi bi-arrow-repeat text-4xl text-blue-600"></i>
          </div>
          <p className="mt-4 text-gray-600">Verificando autenticação...</p>
        </div>
      </div>
    );
  }

  if (!token || !usuario) return null;
  if (requiredRole && !requiredRole.includes(usuario.role)) return null;

  return <>{children}</>;
}
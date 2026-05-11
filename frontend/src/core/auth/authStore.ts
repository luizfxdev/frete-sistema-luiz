import { create } from "zustand";
import { persist } from "zustand/middleware";

export type UserRole = "ADMIN" | "GESTOR" | "MOTORISTA";

export interface AuthUser {
  id: number;
  nome: string;
  email: string;
  role: UserRole;
}

interface AuthState {
  usuario: AuthUser | null;
  token: string | null;
  isLoading: boolean;
  setUsuario: (usuario: AuthUser, token: string) => void;
  logout: () => void;
  hydrate: () => void;
}

function setCookie(value: string) {
  document.cookie = `auth_token=${value}; path=/; max-age=86400; SameSite=Lax`;
}

function clearCookie() {
  document.cookie = "auth_token=; path=/; max-age=0; SameSite=Lax";
}

export const useAuthStore = create<AuthState>()(
  persist(
    (set) => ({
      usuario: null,
      token: null,
      isLoading: true,

      setUsuario: (usuario, token) => {
        setCookie(token);
        set({ usuario, token, isLoading: false });
      },

      logout: () => {
        clearCookie();
        set({ usuario: null, token: null, isLoading: false });
      },

      hydrate: () => {
        
        const state = useAuthStore.getState();
        if (state.token) {
          setCookie(state.token);
        }
        set({ isLoading: false });
      },
    }),
    {
      name: "nextlog-auth",
      partialize: (s) => ({ usuario: s.usuario, token: s.token }),
      onRehydrateStorage: () => (state) => {
        if (state) {
          if (state.token) {
            setCookie(state.token);
          }
          state.isLoading = false;
        }
      },
    }
  )
);
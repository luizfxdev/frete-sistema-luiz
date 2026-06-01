import { useAuthStore } from "@/core/auth/authStore";

const BASE_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080/nextlog";

function getHeaders(isFormData = false): HeadersInit {
  const token = useAuthStore.getState().token;
  const headers: Record<string, string> = {};
  if (!isFormData) headers["Content-Type"] = "application/json";
  if (token) headers["Authorization"] = `Bearer ${token}`;
  return headers;
}

function checkAuthAndRedirect() {
  useAuthStore.getState().logout();
  if (typeof window !== "undefined") {
    const currentPath = window.location.pathname;
    window.location.href = `/auth/login?redirect=${encodeURIComponent(currentPath)}`;
  }
  throw new Error("Autenticação necessária.");
}

async function handleResponse<T>(res: Response): Promise<T> {
  if (res.status === 401 || res.status === 403) {
    checkAuthAndRedirect();
  }

  if (!res.ok) {
    const msg = await res.text().catch(() => res.statusText);
    throw new Error(msg || `HTTP ${res.status}`);
  }

  const text = await res.text();
  const parsed = text ? JSON.parse(text) : ({} as T);
  if (parsed && typeof parsed === "object" && "data" in parsed) {
    return parsed.data as T;
  }
  return parsed;
}

export async function get<T>(path: string): Promise<T> {
  const token = useAuthStore.getState().token;
  if (!token) checkAuthAndRedirect();

  const res = await fetch(`${BASE_URL}${path}`, {
    method: "GET",
    headers: getHeaders(),
    credentials: "include",
  });
  return handleResponse<T>(res);
}

export async function post<T>(path: string, body: unknown): Promise<T> {
  const isForm = body instanceof FormData;
  const res = await fetch(`${BASE_URL}${path}`, {
    method: "POST",
    headers: getHeaders(isForm),
    body: isForm ? body : JSON.stringify(body),
    credentials: "include",
  });
  return handleResponse<T>(res);
}

export async function put<T>(path: string, body: unknown): Promise<T> {
  const res = await fetch(`${BASE_URL}${path}`, {
    method: "PUT",
    headers: getHeaders(),
    body: JSON.stringify(body),
    credentials: "include",
  });
  return handleResponse<T>(res);
}

export async function patch<T>(path: string, body: unknown): Promise<T> {
  const res = await fetch(`${BASE_URL}${path}`, {
    method: "PATCH",
    headers: getHeaders(),
    body: JSON.stringify(body),
    credentials: "include",
  });
  return handleResponse<T>(res);
}

export async function del<T>(path: string): Promise<T> {
  const res = await fetch(`${BASE_URL}${path}`, {
    method: "DELETE",
    headers: getHeaders(),
    credentials: "include",
  });
  return handleResponse<T>(res);
}
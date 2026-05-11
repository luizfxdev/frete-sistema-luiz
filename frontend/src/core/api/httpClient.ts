import { useAuthStore } from "@/core/auth/authStore";

const BASE_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080/nextlog";

function getHeaders(isFormData = false): HeadersInit {
  const token = useAuthStore.getState().token;
  const headers: Record<string, string> = {};
  if (!isFormData) headers["Content-Type"] = "application/json";
  if (token) headers["Authorization"] = `Bearer ${token}`;
  return headers;
}

async function handleResponse<T>(res: Response): Promise<T> {
  if (!res.ok) {
    const msg = await res.text().catch(() => res.statusText);
    throw new Error(msg || `HTTP ${res.status}`);
  }
  const text = await res.text();
  return text ? JSON.parse(text) : ({} as T);
}

export async function get<T>(path: string): Promise<T> {
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
const PUBLIC_API_URL = process.env.NEXT_PUBLIC_API_URL?.replace("/api", "") ?? "http://localhost:8080/nextlog";

async function handleResponse<T>(res: Response): Promise<T> {
  if (!res.ok) {
    const msg = await res.text().catch(() => res.statusText);
    throw new Error(msg || `HTTP ${res.status}`);
  }
  const text = await res.text();
  return text ? JSON.parse(text) : ({} as T);
}

export async function get<T>(path: string): Promise<T> {
  const res = await fetch(`${PUBLIC_API_URL}${path}`, {
    method: "GET",
    headers: { "Content-Type": "application/json" },
    credentials: "include",
  });
  return handleResponse<T>(res);
}

export async function post<T>(path: string, body: unknown): Promise<T> {
  const res = await fetch(`${PUBLIC_API_URL}${path}`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
    credentials: "include",
  });
  return handleResponse<T>(res);
}
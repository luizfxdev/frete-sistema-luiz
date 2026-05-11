import { NextRequest, NextResponse } from "next/server";

const ROTAS_PRIVADAS = ["/dashboard", "/motorista", "/admin"];

export function middleware(request: NextRequest) {
  const { pathname } = request.nextUrl;
  const token = request.cookies.get("auth_token")?.value;

  const ehRotaPrivada = ROTAS_PRIVADAS.some(
    (rota) => pathname === rota || pathname.startsWith(rota + "/")
  );

  if (ehRotaPrivada && !token) {
    return NextResponse.redirect(new URL("/auth/login", request.url));
  }

  return NextResponse.next();
}

export const config = {
  matcher: [
    "/((?!_next/static|_next/image|favicon.ico|api/).*)",
  ],
};
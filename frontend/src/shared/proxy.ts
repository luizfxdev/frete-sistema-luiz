import { NextRequest, NextResponse } from "next/server";

const PUBLIC_PATHS = [
  "/",
  "/cotacao",
  "/colaborador",
  "/trabalhe-conosco",
  "/auth/login",
  "/auth/esqueci-senha",
];

export default function proxy(request: NextRequest) {
  const { pathname } = request.nextUrl;

  const isPublic =
    PUBLIC_PATHS.includes(pathname) ||
    pathname.startsWith("/auth") ||
    pathname.startsWith("/rastreamento") ||
    pathname.startsWith("/_next") ||
    pathname.startsWith("/favicon") ||
    pathname.startsWith("/logo") ||
    pathname.startsWith("/assets") ||
    pathname.startsWith("/video");

  if (isPublic) return NextResponse.next();

  const token = request.cookies.get("nextlog_token")?.value;
  const role = request.cookies.get("nextlog_role")?.value;

  if (!token) {
    const url = request.nextUrl.clone();
    url.pathname = "/colaborador";
    url.searchParams.set("redirect", pathname);
    return NextResponse.redirect(url);
  }

  if (role === "MOTORISTA" && pathname.startsWith("/dashboard")) {
    return NextResponse.redirect(new URL("/motorista/perfil", request.url));
  }

  if ((role === "ADMIN" || role === "GESTOR") && pathname.startsWith("/motorista")) {
    return NextResponse.redirect(new URL("/dashboard", request.url));
  }

  return NextResponse.next();
}

export const config = {
  matcher: ["/((?!_next/static|_next/image|favicon.ico).*)"],
};
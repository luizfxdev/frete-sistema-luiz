package br.com.nextlog.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest  request  = (HttpServletRequest)  req;
        HttpServletResponse response = (HttpServletResponse) res;
        String uri         = request.getRequestURI();
        String contextPath = request.getContextPath();
        String method      = request.getMethod();

        boolean isPublic = uri.equals(contextPath + "/")
                || uri.startsWith(contextPath + "/auth/")
                || (uri.startsWith(contextPath + "/api/candidaturas") && method.equals("POST"))
                || uri.startsWith(contextPath + "/api/rastreamento")
                || uri.startsWith(contextPath + "/api/cotacao")
                || uri.startsWith(contextPath + "/api/dashboard/")
                || uri.startsWith(contextPath + "/static/")
                || uri.endsWith(".css")
                || uri.endsWith(".js")
                || uri.endsWith(".svg");

        if (isPublic) {
            chain.doFilter(req, res);
            return;
        }

        HttpSession session = request.getSession(false);
        boolean logado = session != null && session.getAttribute("usuarioLogado") != null;

        if (!logado) {
            response.sendRedirect(contextPath + "/auth/login");
            return;
        }

        String role = (String) session.getAttribute("usuarioRole");
        boolean isRotaMotorista = uri.contains("/motorista/dashboard")
                || uri.contains("/api/motorista");

        if ("MOTORISTA".equals(role) && !isRotaMotorista) {
            response.sendRedirect(contextPath + "/motorista/dashboard");
            return;
        }

        chain.doFilter(req, res);
    }

    @Override public void init(FilterConfig config) {}
    @Override public void destroy() {}
}
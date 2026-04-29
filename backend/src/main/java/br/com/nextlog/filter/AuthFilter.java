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

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();

        boolean isPublic = uri.equals(contextPath + "/")
                || uri.startsWith(contextPath + "/auth/")
                || uri.startsWith(contextPath + "/static/")
                || uri.startsWith(contextPath + "/api/")
                || uri.endsWith(".css")
                || uri.endsWith(".js")
                || uri.endsWith(".svg");

        if (isPublic) { chain.doFilter(req, res); return; }

        HttpSession session = request.getSession(false);
        boolean logado = session != null && session.getAttribute("usuarioLogado") != null;

        if (!logado) {
            response.sendRedirect(contextPath + "/auth/login");
            return;
        }
        chain.doFilter(req, res);
    }

    @Override public void init(FilterConfig config) {}
    @Override public void destroy() {}
}
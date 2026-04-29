package br.com.nextlog.auth;

import br.com.nextlog.exception.AuthException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet({"/auth/login", "/auth/logout"})
public class LoginControlador extends HttpServlet {

    private final LoginBO loginBO = new LoginBO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (req.getServletPath().endsWith("/logout")) {
            loginBO.encerrarSessao(req.getSession(false));
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");
        String senha = req.getParameter("senha");

        try {
            loginBO.autenticar(email, senha, req.getSession(true));
            resp.sendRedirect(req.getContextPath() + "/fretes");
        } catch (AuthException e) {
            req.setAttribute("erro", e.getMessage());
            req.setAttribute("email", email);
            req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
        }
    }
}
package br.com.nextlog.auth;

import br.com.nextlog.exception.AuthException;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginBO {

    private static final Logger LOG = Logger.getLogger(LoginBO.class.getName());
    private final LoginDAO loginDAO = new LoginDAO();

    public void autenticar(String email, String senha, HttpSession session) {
        if (email == null || email.trim().isEmpty()) {
            throw new AuthException("O e-mail é obrigatório.");
        }
        if (senha == null || senha.trim().isEmpty()) {
            throw new AuthException("A senha é obrigatória.");
        }

        try {
            Login usuario = loginDAO.buscarPorEmail(email.trim());
            if (usuario == null || !usuario.isAtivo() || !senha.equals(usuario.getSenha())) {
                throw new AuthException("E-mail ou senha inválidos.");
            }
            session.setAttribute("usuarioLogado", usuario.getEmail());
            session.setAttribute("usuarioNome",   usuario.getNome());
            session.setAttribute("usuarioId",     usuario.getId());
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Falha ao autenticar usuário", e);
            throw new AuthException("Erro ao autenticar. Tente novamente.");
        }
    }

    public void encerrarSessao(HttpSession session) {
        if (session != null) session.invalidate();
    }
}
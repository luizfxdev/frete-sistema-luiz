package br.com.nextlog.auth;

import br.com.nextlog.exception.AuthException;
import br.com.nextlog.util.JsonResponse;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet({"/auth/login", "/auth/logout"})
public class LoginControlador extends HttpServlet {

    private final LoginBO loginBO = new LoginBO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getServletPath();
        
        if ("/auth/logout".equals(path)) {
            loginBO.encerrarSessao(req.getSession(false));
            Map<String, Boolean> payload = new HashMap<>();
            payload.put("sucesso", true);
            JsonResponse.ok(resp, payload);
            return;
        }
        
        JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST, 
            "Use POST para fazer login.");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getServletPath();
        
        if ("/auth/logout".equals(path)) {
            loginBO.encerrarSessao(req.getSession(false));
            Map<String, Boolean> payload = new HashMap<>();
            payload.put("sucesso", true);
            JsonResponse.ok(resp, payload);
            return;
        }

        try {
            String body = req.getReader().lines().reduce("", (a, l) -> a + l);
            String email = extrairString(body, "email");
            String senha = extrairString(body, "senha");

            if (email == null || senha == null) {
                JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST,
                    "E-mail e senha são obrigatórios.");
                return;
            }

            HttpSession session = req.getSession(true);
            LoginUsuario u = loginBO.autenticar(email, senha, session);
            String token = session.getId();

            Map<String, Object> payload = new HashMap<>();
            payload.put("id", u.getId());
            payload.put("nome", u.getNome());
            payload.put("email", u.getEmail());
            payload.put("role", u.getRole());
            payload.put("token", token);
            
            JsonResponse.ok(resp, payload);
        } catch (AuthException e) {
            JsonResponse.erro(resp, HttpServletResponse.SC_UNAUTHORIZED,
                "E-mail ou senha inválidos.");
        } catch (Exception e) {
            JsonResponse.erro(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "Erro ao autenticar.");
        }
    }

    private String extrairString(String json, String campo) {
        String chave = "\"" + campo + "\":\"";
        int idx = json.indexOf(chave);
        if (idx < 0) return null;
        int start = idx + chave.length();
        int end = json.indexOf("\"", start);
        if (end < 0) return null;
        return json.substring(start, end);
    }
}
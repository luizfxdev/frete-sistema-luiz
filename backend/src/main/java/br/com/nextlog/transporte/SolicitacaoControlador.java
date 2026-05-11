package br.com.nextlog.transporte;

import br.com.nextlog.exception.NegocioException;
import br.com.nextlog.util.JsonResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/solicitacoes/*")
public class SolicitacaoControlador extends HttpServlet {

    private final SolicitacaoBO solicitacaoBO = new SolicitacaoBO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            SolicitacaoTransporte s = parseBody(req);
            Long id = solicitacaoBO.enviar(s);
            JsonResponse.ok(resp, "{\"id\":" + id + "}");
        } catch (NegocioException e) {
            JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo();
        try {
            if (path != null && path.startsWith("/motorista/")) {
                Long idMotorista = Long.valueOf(path.substring("/motorista/".length()));
                List<SolicitacaoTransporte> lista = solicitacaoBO.listarPorMotorista(idMotorista);
                JsonResponse.ok(resp, toJson(lista));
            } else {
                JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST, "Rota inválida.");
            }
        } catch (NegocioException e) {
            JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo();
        HttpSession session = req.getSession(false);
        Long idMotorista = session != null ? (Long) session.getAttribute("usuarioId") : null;

        try {
            if (path != null && path.endsWith("/aceitar")) {
                Long id = extrairIdDePath(path, "/aceitar");
                solicitacaoBO.aceitar(id, idMotorista);
                JsonResponse.ok(resp, "{\"sucesso\":true}");
            } else if (path != null && path.endsWith("/recusar")) {
                Long id = extrairIdDePath(path, "/recusar");
                String body = req.getReader().lines().reduce("", (a, l) -> a + l);
                String motivo = extrairString(body, "motivo");
                solicitacaoBO.recusar(id, idMotorista, motivo);
                JsonResponse.ok(resp, "{\"sucesso\":true}");
            } else {
                JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST, "Rota inválida.");
            }
        } catch (NegocioException e) {
            JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    private SolicitacaoTransporte parseBody(HttpServletRequest req) throws IOException {
        String body = req.getReader().lines().reduce("", (a, l) -> a + l);
        SolicitacaoTransporte s = new SolicitacaoTransporte();
        s.setIdMotorista(extrairLong(body, "idMotorista"));
        s.setMunicipioColeta(extrairString(body, "municipioColeta"));
        s.setUfColeta(extrairString(body, "ufColeta"));
        s.setLogradouroColeta(extrairString(body, "logradouroColeta"));
        s.setMunicipioDestino(extrairString(body, "municipioDestino"));
        s.setUfDestino(extrairString(body, "ufDestino"));
        s.setLogradouroDestino(extrairString(body, "logradouroDestino"));
        s.setDescricaoCarga(extrairString(body, "descricaoCarga"));
        return s;
    }

    private String toJson(List<SolicitacaoTransporte> lista) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < lista.size(); i++) {
            SolicitacaoTransporte s = lista.get(i);
            sb.append("{")
              .append("\"id\":").append(s.getId()).append(",")
              .append("\"idMotorista\":").append(s.getIdMotorista()).append(",")
              .append("\"municipioColeta\":\"").append(s.getMunicipioColeta()).append("\",")
              .append("\"municipioDestino\":\"").append(s.getMunicipioDestino()).append("\",")
              .append("\"status\":\"").append(s.getStatus()).append("\"")
              .append("}");
            if (i < lista.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    private Long extrairIdDePath(String path, String sufixo) {
        return Long.valueOf(path.replace(sufixo, "").replace("/", ""));
    }

    private Long extrairLong(String json, String campo) {
        String chave = "\"" + campo + "\":";
        int idx = json.indexOf(chave);
        if (idx < 0) return null;
        int start = idx + chave.length();
        int end = json.indexOf(",", start);
        if (end < 0) end = json.indexOf("}", start);
        return Long.valueOf(json.substring(start, end).trim());
    }

    private String extrairString(String json, String campo) {
        String chave = "\"" + campo + "\":\"";
        int idx = json.indexOf(chave);
        if (idx < 0) return null;
        int start = idx + chave.length();
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }
}
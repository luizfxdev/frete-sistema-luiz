package br.com.nextlog.manutencao.orcamento;

import br.com.nextlog.exception.NegocioException;
import br.com.nextlog.util.JsonResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/orcamentos/*")
public class OrcamentoControlador extends HttpServlet {

    private final OrcamentoBO orcamentoBO = new OrcamentoBO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getPathInfo();
        try {
            if (path != null && path.startsWith("/manutencao/")) {
                Long idManutencao = Long.valueOf(path.substring("/manutencao/".length()));
                List<OrcamentoManutencao> lista = orcamentoBO.buscarPorManutencao(idManutencao);
                JsonResponse.ok(resp, toJson(lista));
            } else {
                JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST, "Rota inválida.");
            }
        } catch (NegocioException e) {
            JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            OrcamentoManutencao o = parseBody(req);
            Long id = orcamentoBO.registrarOrcamento(o);
            JsonResponse.ok(resp, "{\"id\":" + id + "}");
        } catch (NegocioException e) {
            JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo();
        try {
            Long id = Long.valueOf(path.substring(1));
            orcamentoBO.excluir(id);
            JsonResponse.ok(resp, "{\"sucesso\":true}");
        } catch (NegocioException e) {
            JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    private OrcamentoManutencao parseBody(HttpServletRequest req) throws IOException {
        String body = req.getReader().lines().reduce("", (a, l) -> a + l);
        OrcamentoManutencao o = new OrcamentoManutencao();
        o.setIdManutencao(extrairLong(body, "idManutencao"));
        o.setObservacao(extrairString(body, "observacao"));
        return o;
    }

    private String toJson(List<OrcamentoManutencao> lista) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < lista.size(); i++) {
            OrcamentoManutencao o = lista.get(i);
            sb.append("{")
              .append("\"id\":").append(o.getId()).append(",")
              .append("\"numero\":\"").append(o.getNumero()).append("\",")
              .append("\"idManutencao\":").append(o.getIdManutencao()).append(",")
              .append("\"valorTotal\":").append(o.getValorTotal()).append(",")
              .append("\"observacao\":").append(o.getObservacao() == null ? "null" : "\"" + o.getObservacao() + "\"")
              .append("}");
            if (i < lista.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
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
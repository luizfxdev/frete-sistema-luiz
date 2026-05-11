package br.com.nextlog.candidatura;

import br.com.nextlog.exception.NegocioException;
import br.com.nextlog.util.JsonResponse;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@WebServlet("/api/candidaturas/*")
public class CandidaturaControlador extends HttpServlet {

    private final CandidaturaBO candidaturaBO = new CandidaturaBO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            CandidaturaMotorista c = parseBody(req);
            candidaturaBO.registrar(c);
            JsonResponse.ok(resp, "{\"sucesso\":true,\"mensagem\":\"Candidatura registrada com sucesso.\"}");
        } catch (NegocioException e) {
            JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (DateTimeParseException e) {
            JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST, "Data inválida.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<CandidaturaMotorista> lista = candidaturaBO.listar();
            JsonResponse.ok(resp, toJson(lista));
        } catch (NegocioException e) {
            JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        try {
            if (path != null && path.endsWith("/aprovar")) {
                Long id = extrairIdDePath(path, "/aprovar");
                candidaturaBO.aprovar(id);
                JsonResponse.ok(resp, "{\"sucesso\":true}");
            } else if (path != null && path.endsWith("/recusar")) {
                Long id = extrairIdDePath(path, "/recusar");
                candidaturaBO.recusar(id);
                JsonResponse.ok(resp, "{\"sucesso\":true}");
            } else {
                JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST, "Rota inválida.");
            }
        } catch (NegocioException e) {
            JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    private CandidaturaMotorista parseBody(HttpServletRequest req) throws IOException {
        String body = req.getReader().lines().reduce("", (a, l) -> a + l);
        CandidaturaMotorista c = new CandidaturaMotorista();
        c.setNome(extrairString(body, "nome"));
        c.setCpf(extrairString(body, "cpf"));
        String dataNasc = extrairString(body, "dataNascimento");
        if (dataNasc != null) c.setDataNascimento(LocalDate.parse(dataNasc));
        c.setTelefone(extrairString(body, "telefone"));
        c.setEmail(extrairString(body, "email"));
        c.setCnhNumero(extrairString(body, "cnhNumero"));
        c.setCnhCategoria(extrairString(body, "cnhCategoria"));
        String validade = extrairString(body, "cnhValidade");
        if (validade != null) c.setCnhValidade(LocalDate.parse(validade));
        c.setTipoVinculo(extrairString(body, "tipoVinculo"));
        c.setMunicipio(extrairString(body, "municipio"));
        c.setUf(extrairString(body, "uf"));
        c.setMensagem(extrairString(body, "mensagem"));
        return c;
    }

    private String toJson(List<CandidaturaMotorista> lista) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < lista.size(); i++) {
            CandidaturaMotorista c = lista.get(i);
            sb.append("{")
              .append("\"id\":").append(c.getId()).append(",")
              .append("\"nome\":\"").append(escape(c.getNome())).append("\",")
              .append("\"cpf\":\"").append(escape(c.getCpf())).append("\",")
              .append("\"status\":\"").append(c.getStatus()).append("\",")
              .append("\"cnhCategoria\":\"").append(escape(c.getCnhCategoria())).append("\",")
              .append("\"municipio\":\"").append(escape(c.getMunicipio())).append("\",")
              .append("\"uf\":\"").append(escape(c.getUf())).append("\"")
              .append("}");
            if (i < lista.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    private Long extrairIdDePath(String path, String sufixo) {
        return Long.valueOf(path.replace(sufixo, "").replace("/", ""));
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

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
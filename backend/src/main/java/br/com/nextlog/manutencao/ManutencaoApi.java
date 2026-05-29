package br.com.nextlog.manutencao;

import br.com.nextlog.exception.NegocioException;
import br.com.nextlog.manutencao.orcamento.OrcamentoManutencaoItem;
import br.com.nextlog.util.JsonResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/manutencoes/*")
public class ManutencaoApi extends HttpServlet {

    private final ManutencaoBO manutencaoBO = new ManutencaoBO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        String path = req.getPathInfo();
        
        try {
            if (path != null && !path.equals("/")) {
                Long id = Long.valueOf(path.substring(1));
                
                ManutencaoVeiculo manutencao = manutencaoBO.buscarPorId(id);
                if (manutencao == null) {
                    JsonResponse.erro(resp, HttpServletResponse.SC_NOT_FOUND, "Manutenção não encontrada");
                    return;
                }
                
                List<OrcamentoManutencaoItem> itens = manutencaoBO.buscarOrcamentoItens(id);
                
                String json = "{" +
                    "\"manutencao\":" + toJsonManutencao(manutencao) + "," +
                    "\"orcamentos\":" + toJsonItens(itens) +
                    "}";
                
                JsonResponse.ok(resp, json);
            } else {
                JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST, "ID obrigatório");
            }
        } catch (NumberFormatException e) {
            JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
        } catch (NegocioException e) {
            JsonResponse.erro(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        String path = req.getPathInfo();
        
        try {
            if (path == null || !path.contains("/")) {
                JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST, "Rota inválida");
                return;
            }
            
            String[] parts = path.split("/");
            Long id = Long.valueOf(parts[1]);
            String acao = parts.length > 2 ? parts[2] : "";
            
            if ("liberar".equals(acao)) {
                manutencaoBO.liberarManutencao(id);
                JsonResponse.ok(resp, "{\"sucesso\":true,\"mensagem\":\"Veículo liberado com sucesso\"}");
            } else if ("cancelar".equals(acao)) {
                manutencaoBO.cancelarManutencao(id);
                JsonResponse.ok(resp, "{\"sucesso\":true,\"mensagem\":\"Manutenção cancelada com sucesso\"}");
            } else {
                JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST, "Ação inválida");
            }
        } catch (NumberFormatException e) {
            JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
        } catch (NegocioException e) {
            JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    private String toJsonManutencao(ManutencaoVeiculo m) {
        return "{" +
            "\"id\":" + m.getId() + "," +
            "\"placa\":\"" + escape(m.getPlaca()) + "\"," +
            "\"tipo\":\"" + m.getTipo().name() + "\"," +
            "\"status\":\"" + m.getStatusManutencao().name() + "\"," +
            "\"statusManutencao\":\"" + m.getStatusManutencao().name() + "\"," +
            "\"dataInicio\":\"" + m.getDataInicio().toString() + "\"," +
            (m.getDataFim() != null ? "\"dataFim\":\"" + m.getDataFim().toString() + "\"," : "") +
            "\"kmAtual\":" + (m.getKmAtual() != null ? m.getKmAtual().doubleValue() : 0) + "," +
            "\"custo\":" + (m.getCusto() != null ? m.getCusto().doubleValue() : 0) + "," +
            "\"descricao\":\"" + escape(m.getDescricao()) + "\"" +
            "}";
    }

    private String toJsonItens(List<OrcamentoManutencaoItem> itens) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < itens.size(); i++) {
            OrcamentoManutencaoItem item = itens.get(i);
            sb.append("{")
              .append("\"id\":").append(item.getId()).append(",")
              .append("\"descricao\":\"").append(escape(item.getDescricao())).append("\",")
              .append("\"quantidade\":").append(item.getQuantidade()).append(",")
              .append("\"precoUnitario\":").append(item.getPrecoUnitario()).append(",")
              .append("\"valorTotal\":").append(item.getValorTotal())
              .append("}");
            if (i < itens.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    private String escape(String str) {
        if (str == null) return "";
        return str.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
}
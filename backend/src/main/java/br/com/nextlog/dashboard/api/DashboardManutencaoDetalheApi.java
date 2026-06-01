package br.com.nextlog.dashboard.api;

import br.com.nextlog.util.JsonResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/dashboard/manutencoes/*")
public class DashboardManutencaoDetalheApi extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    String path = req.getPathInfo();
    if (path == null || path.equals("/")) {
      JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST, "ID obrigatório.");
      return;
    }

    try {
      Long id = Long.valueOf(path.substring(1));
      retornarDetalhe(id, resp);
    } catch (NumberFormatException e) {
      JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST, "ID inválido.");
    } catch (Exception e) {
      e.printStackTrace();
      JsonResponse.erro(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
          "Erro ao buscar manutenção: " + e.getMessage());
    }
  }

  private void retornarDetalhe(Long id, HttpServletResponse resp) throws IOException {

    Map<String, Object> manutencao = new LinkedHashMap<>();
    manutencao.put("id", id);
    manutencao.put("idVeiculo", 1L);
    manutencao.put("placa", "ABC-" + id + "234");
    manutencao.put("tipo", "PREVENTIVA");
    manutencao.put("dataInicio", "2026-03-14");
    manutencao.put("dataFim", null);
    manutencao.put("kmAtual", 45250);
    manutencao.put("custo", 850.00);
    manutencao.put("descricao", "Manutenção preventiva regular");
    manutencao.put("statusManutencao", "EM_ANDAMENTO");
    manutencao.put("dataCriacao", "2026-03-14");
    manutencao.put("dataAtualizacao", "2026-03-14");


    List<Map<String, Object>> itens = new ArrayList<>();
    Map<String, Object> item1 = new LinkedHashMap<>();
    item1.put("id", 1L);
    item1.put("descricao", "Troca de óleo");
    item1.put("quantidade", 1);
    item1.put("precoUnitario", 150.00);
    item1.put("valorTotal", 150.00);
    itens.add(item1);

    Map<String, Object> item2 = new LinkedHashMap<>();
    item2.put("id", 2L);
    item2.put("descricao", "Filtro de ar");
    item2.put("quantidade", 2);
    item2.put("precoUnitario", 350.00);
    item2.put("valorTotal", 700.00);
    itens.add(item2);

    
    List<Map<String, Object>> orcamentos = new ArrayList<>();
    Map<String, Object> orcamento = new LinkedHashMap<>();
    orcamento.put("id", 1L);
    orcamento.put("numero", "ORC-2026-001");
    orcamento.put("observacao", "Orçamento de manutenção");
    orcamento.put("valorTotal", 850.00);
    orcamento.put("itens", itens);
    orcamentos.add(orcamento);

   
    Map<String, Object> data = new LinkedHashMap<>();
    data.put("manutencao", manutencao);
    data.put("orcamentos", orcamentos);

    Map<String, Object> response = new LinkedHashMap<>();
    response.put("success", true);
    response.put("data", data);

    JsonResponse.ok(resp, response);
  }
}
package br.com.nextlog.dashboard.api;

import br.com.nextlog.enums.StatusManutencao;
import br.com.nextlog.enums.TipoManutencao;
import br.com.nextlog.manutencao.ManutencaoBO;
import br.com.nextlog.manutencao.ManutencaoVeiculo;
import br.com.nextlog.exception.NegocioException;
import br.com.nextlog.util.JsonResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/dashboard/manutencao/*")
public class DashboardManutencaoApi extends HttpServlet {

  private final ManutencaoBO manutencaoBO = new ManutencaoBO();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    String path = req.getPathInfo();
    if (path == null) path = "/";

    try {

      if ("/kpis".equals(path)) {
        retornarKpis(resp);
        return;
      }

      if ("/lista".equals(path)) {
        retornarLista(req, resp);
        return;
      }

      JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST, "Rota inválida.");
    } catch (Exception e) {
      JsonResponse.erro(
          resp,
          HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
          "Erro ao processar requisição.");
    }
  }

  private void retornarKpis(HttpServletResponse resp) throws IOException {
    int emManutencao = manutencaoBO.contarPorStatus(StatusManutencao.EM_ANDAMENTO);
    int preventiva = manutencaoBO.contarPorTipo(TipoManutencao.PREVENTIVA);
    int corretiva = manutencaoBO.contarPorTipo(TipoManutencao.CORRETIVA);
    int liberados = manutencaoBO.contarLiberadasNoMes();

    Map<String, Object> data = new LinkedHashMap<>();
    data.put("emManutencao", emManutencao);
    data.put("preventiva", preventiva);
    data.put("corretiva", corretiva);
    data.put("liberados", liberados);

    Map<String, Object> response = new LinkedHashMap<>();
    response.put("success", true);
    response.put("data", data);

    JsonResponse.ok(resp, response);
  }

  private void retornarLista(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    try {
      String filtro = req.getParameter("filtro");
      int pagina = parseIntOu(req.getParameter("pagina"), 1);
      int tamanhoPagina = 10;

      List<ManutencaoVeiculo> lista = manutencaoBO.listar(filtro, pagina, tamanhoPagina);
      int total = manutencaoBO.contar(filtro);
      int totalPaginas = Math.max((int) Math.ceil((double) total / tamanhoPagina), 1);

      // Montar a resposta
      Map<String, Object> dataMap = new LinkedHashMap<>();
      dataMap.put("data", toJsonList(lista));
      dataMap.put("pagination", new LinkedHashMap<String, Object>() {{
        put("pagina", pagina);
        put("totalPaginas", totalPaginas);
        put("total", total);
      }});

      Map<String, Object> response = new LinkedHashMap<>();
      response.put("success", true);
      response.put("data", dataMap);

      JsonResponse.ok(resp, response);
    } catch (Exception e) {
      JsonResponse.erro(
          resp,
          HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
          "Erro ao listar manutenções.");
    }
  }

  private List<Map<String, Object>> toJsonList(List<ManutencaoVeiculo> lista) {
    List<Map<String, Object>> resultado = new java.util.ArrayList<>();
    for (ManutencaoVeiculo m : lista) {
      Map<String, Object> item = new LinkedHashMap<>();
      item.put("id", m.getId());
      item.put("idVeiculo", m.getIdVeiculo());
      item.put("tipo", m.getTipo().name());
      item.put("placa", m.getPlaca());
      item.put("dataInicio", m.getDataInicio().toString());
      item.put("dataFim", m.getDataFim() != null ? m.getDataFim().toString() : null);
      item.put("kmAtual", m.getKmAtual());
      item.put("custo", m.getCusto());
      item.put("descricao", m.getDescricao());
      item.put("statusManutencao", m.getStatusManutencao().name());
      item.put("dataCriacao", m.getDataCriacao() != null ? m.getDataCriacao().toString() : null);
      item.put("dataAtualizacao", m.getDataAtualizacao() != null ? m.getDataAtualizacao().toString() : null);
      resultado.add(item);
    }
    return resultado;
  }

  private int parseIntOu(String v, int padrao) {
    try {
      return v == null ? padrao : Integer.parseInt(v);
    } catch (NumberFormatException e) {
      return padrao;
    }
  }
}
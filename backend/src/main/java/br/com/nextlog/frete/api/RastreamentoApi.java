package br.com.nextlog.frete.api;

import br.com.nextlog.frete.Frete;
import br.com.nextlog.frete.FreteBO;
import br.com.nextlog.frete.ocorrencia.OcorrenciaDAO;
import br.com.nextlog.frete.ocorrencia.OcorrenciaFrete;
import br.com.nextlog.util.JsonResponse;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/rastreamento/*")
public class RastreamentoApi extends HttpServlet {

    private final FreteBO freteBO = new FreteBO();
    private final OcorrenciaDAO ocorrenciaDAO = new OcorrenciaDAO();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST,
                    "Número do frete é obrigatório (ex: /api/rastreamento/FRT-2026-00001)");
            return;
        }

        String numero = pathInfo.substring(1).trim().toUpperCase();

        if (!validarFormatoNumero(numero)) {
            JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST,
                    "Formato inválido. Use o formato: FRT-AAAA-NNNNN (ex: FRT-2026-00001)");
            return;
        }

        try {
            Frete frete = freteBO.buscarPorNumero(numero);

            if (frete == null) {
                JsonResponse.erro(resp, HttpServletResponse.SC_NOT_FOUND,
                        "Frete não encontrado");
                return;
            }

            List<OcorrenciaFrete> ocorrencias = ocorrenciaDAO.listarPorFrete(frete.getId());

            Map<String, Object> resposta = new LinkedHashMap<>();
            resposta.put("sucesso", true);

            Map<String, Object> freteData = new LinkedHashMap<>();
            freteData.put("numero", frete.getNumero());
            freteData.put("status", frete.getStatus().name());
            freteData.put("remetente", frete.getRemetenteRazaoSocial());
            freteData.put("destinatario", frete.getDestinatarioRazaoSocial());
            freteData.put("municipioOrigem", frete.getMunicipioOrigem());
            freteData.put("ufOrigem", frete.getUfOrigem());
            freteData.put("municipioDestino", frete.getMunicipioDestino());
            freteData.put("ufDestino", frete.getUfDestino());
            freteData.put("dataEmissao", frete.getDataEmissao().format(DATE_FORMATTER));
            freteData.put("dataPrevisaoEntrega", frete.getDataPrevisaoEntrega().format(DATE_FORMATTER));
            freteData.put("dataSaida", frete.getDataSaida() != null ? frete.getDataSaida().format(DATETIME_FORMATTER) : null);
            freteData.put("dataEntrega", frete.getDataEntrega() != null ? frete.getDataEntrega().format(DATETIME_FORMATTER) : null);

            resposta.put("frete", freteData);

            List<Map<String, Object>> ocorrenciasData = ocorrencias.stream()
                    .map(this::mapearOcorrencia)
                    .toList();

            resposta.put("ocorrencias", ocorrenciasData);

            JsonResponse.ok(resp, resposta);

        } catch (Exception e) {
            JsonResponse.erro(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Erro ao consultar rastreamento. Tente novamente.");
        }
    }

    private boolean validarFormatoNumero(String numero) {
        if (numero == null || numero.isEmpty()) return false;
        
        return numero.matches("^FRT-\\d{4}-\\d{5}$");
    }

    private Map<String, Object> mapearOcorrencia(OcorrenciaFrete o) {
        Map<String, Object> mapa = new LinkedHashMap<>();
        mapa.put("tipo", o.getTipo().name());
        mapa.put("dataHora", o.getDataHora().format(DATETIME_FORMATTER));
        mapa.put("municipio", o.getMunicipio());
        mapa.put("uf", o.getUf());
        mapa.put("descricao", o.getDescricao());
        return mapa;
    }
}
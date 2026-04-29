package br.com.nextlog.frete.ocorrencia.api;

import br.com.nextlog.enums.TipoOcorrencia;
import br.com.nextlog.exception.NegocioException;
import br.com.nextlog.frete.ocorrencia.OcorrenciaBO;
import br.com.nextlog.frete.ocorrencia.OcorrenciaFrete;
import br.com.nextlog.util.JsonResponse;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/api/ocorrencias/*")
public class OcorrenciaApi extends HttpServlet {

    private final OcorrenciaBO ocorrenciaBO = new OcorrenciaBO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            OcorrenciaFrete o = new OcorrenciaFrete();
            String idFrete = req.getParameter("idFrete");
            if (idFrete == null || idFrete.isEmpty()) {
                JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST, "Frete é obrigatório.");
                return;
            }
            o.setIdFrete(Long.valueOf(idFrete));
            String tipo = req.getParameter("tipo");
            if (tipo != null && !tipo.isEmpty()) o.setTipo(TipoOcorrencia.valueOf(tipo));
            String dh = req.getParameter("dataHora");
            o.setDataHora(dh == null || dh.isEmpty() ? LocalDateTime.now() : LocalDateTime.parse(dh));
            o.setMunicipio(req.getParameter("municipio"));
            String uf = req.getParameter("uf");
            o.setUf(uf == null ? null : uf.toUpperCase());
            o.setDescricao(req.getParameter("descricao"));
            o.setNomeRecebedor(req.getParameter("nomeRecebedor"));
            o.setDocumentoRecebedor(req.getParameter("documentoRecebedor"));

            Long id = ocorrenciaBO.registrarOcorrencia(o);

            Map<String, Object> r = new LinkedHashMap<>();
            r.put("id", id);
            r.put("ok", true);
            JsonResponse.ok(resp, r);
        } catch (NegocioException e) {
            JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            JsonResponse.erro(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro interno.");
        }
    }
}
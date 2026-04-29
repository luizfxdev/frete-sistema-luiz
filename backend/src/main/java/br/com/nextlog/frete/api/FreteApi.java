package br.com.nextlog.frete.api;

import br.com.nextlog.exception.NegocioException;
import br.com.nextlog.frete.Frete;
import br.com.nextlog.frete.FreteBO;
import br.com.nextlog.frete.ocorrencia.OcorrenciaBO;
import br.com.nextlog.frete.ocorrencia.OcorrenciaFrete;
import br.com.nextlog.util.JsonResponse;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/fretes/*")
public class FreteApi extends HttpServlet {

    private final FreteBO freteBO = new FreteBO();
    private final OcorrenciaBO ocorrenciaBO = new OcorrenciaBO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if (path == null || "/".equals(path)) {
            JsonResponse.erro(resp, HttpServletResponse.SC_NOT_FOUND, "Recurso não encontrado.");
            return;
        }

        try {
            String[] partes = path.substring(1).split("/");
            Long id = Long.valueOf(partes[0]);

            if (partes.length == 1) {
                Frete f = freteBO.buscarPorId(id);
                if (f == null) { JsonResponse.erro(resp, HttpServletResponse.SC_NOT_FOUND, "Frete não encontrado."); return; }
                JsonResponse.ok(resp, freteToMap(f));
                return;
            }
            if (partes.length == 2 && "ocorrencias".equals(partes[1])) {
                List<OcorrenciaFrete> oc = ocorrenciaBO.listarPorFrete(id);
                JsonResponse.ok(resp, oc);
                return;
            }
            JsonResponse.erro(resp, HttpServletResponse.SC_NOT_FOUND, "Recurso não encontrado.");
        } catch (NumberFormatException e) {
            JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST, "ID inválido.");
        } catch (NegocioException e) {
            JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            JsonResponse.erro(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro interno.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if (path == null || "/".equals(path)) {
            JsonResponse.erro(resp, HttpServletResponse.SC_NOT_FOUND, "Recurso não encontrado.");
            return;
        }

        try {
            String[] partes = path.substring(1).split("/");
            if (partes.length < 2) {
                JsonResponse.erro(resp, HttpServletResponse.SC_NOT_FOUND, "Ação não especificada.");
                return;
            }
            Long id = Long.valueOf(partes[0]);
            String acao = partes[1];

            switch (acao) {
                case "confirmar-saida":
                    freteBO.confirmarSaida(id);
                    JsonResponse.ok(resp, mensagemOk("Saída confirmada."));
                    return;
                case "registrar-entrega":
                    freteBO.registrarEntrega(id);
                    JsonResponse.ok(resp, mensagemOk("Entrega registrada."));
                    return;
                case "cancelar":
                    freteBO.cancelar(id);
                    JsonResponse.ok(resp, mensagemOk("Frete cancelado."));
                    return;
                default:
                    JsonResponse.erro(resp, HttpServletResponse.SC_NOT_FOUND, "Ação inválida.");
            }
        } catch (NumberFormatException e) {
            JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST, "ID inválido.");
        } catch (NegocioException e) {
            JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            JsonResponse.erro(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro interno.");
        }
    }

    private Map<String, Object> mensagemOk(String msg) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("ok", true);
        m.put("mensagem", msg);
        return m;
    }

    private Map<String, Object> freteToMap(Frete f) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", f.getId());
        m.put("numero", f.getNumero());
        m.put("status", f.getStatus());
        m.put("remetente", f.getRemetenteRazaoSocial());
        m.put("destinatario", f.getDestinatarioRazaoSocial());
        m.put("motorista", f.getMotoristaNome());
        m.put("veiculoPlaca", f.getVeiculoPlaca());
        m.put("origem", f.getMunicipioOrigem() + "/" + f.getUfOrigem());
        m.put("destino", f.getMunicipioDestino() + "/" + f.getUfDestino());
        m.put("descricaoCarga", f.getDescricaoCarga());
        m.put("pesoKg", f.getPesoKg());
        m.put("volumes", f.getVolumes());
        m.put("valorFrete", f.getValorFrete());
        m.put("aliquotaIcms", f.getAliquotaIcms());
        m.put("valorIcms", f.getValorIcms());
        m.put("valorTotal", f.getValorTotal());
        m.put("dataEmissao", f.getDataEmissao());
        m.put("dataPrevisaoEntrega", f.getDataPrevisaoEntrega());
        m.put("dataSaida", f.getDataSaida());
        m.put("dataEntrega", f.getDataEntrega());
        return m;
    }
}
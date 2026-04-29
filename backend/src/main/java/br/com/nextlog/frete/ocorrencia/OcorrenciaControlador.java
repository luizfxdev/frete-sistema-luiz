package br.com.nextlog.frete.ocorrencia;

import br.com.nextlog.enums.TipoOcorrencia;
import br.com.nextlog.exception.NegocioException;
import br.com.nextlog.frete.Frete;
import br.com.nextlog.frete.FreteBO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/ocorrencias/*")
public class OcorrenciaControlador extends HttpServlet {

    private final OcorrenciaBO ocorrenciaBO = new OcorrenciaBO();
    private final FreteBO freteBO = new FreteBO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String acao = req.getPathInfo();
        if (acao != null && acao.startsWith("/novo/")) {
            Long idFrete = Long.valueOf(acao.substring("/novo/".length()));
            Frete frete = freteBO.buscarPorId(idFrete);
            if (frete == null) { resp.sendRedirect(req.getContextPath() + "/fretes"); return; }
            OcorrenciaFrete o = new OcorrenciaFrete();
            o.setIdFrete(idFrete);
            o.setMunicipio(frete.getMunicipioDestino());
            o.setUf(frete.getUfDestino());
            req.setAttribute("ocorrencia", o);
            req.setAttribute("frete", frete);
            req.setAttribute("tipos", TipoOcorrencia.values());
            req.getRequestDispatcher("/WEB-INF/views/frete/ocorrencia/form.jsp").forward(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/fretes");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long idFrete = null;
        try {
            OcorrenciaFrete o = bind(req);
            idFrete = o.getIdFrete();
            ocorrenciaBO.registrarOcorrencia(o);
            resp.sendRedirect(req.getContextPath() + "/fretes/detalhe/" + idFrete);
        } catch (NegocioException e) {
            req.setAttribute("erro", e.getMessage());
            OcorrenciaFrete o = bind(req);
            req.setAttribute("ocorrencia", o);
            if (idFrete == null) idFrete = o.getIdFrete();
            if (idFrete != null) req.setAttribute("frete", freteBO.buscarPorId(idFrete));
            req.setAttribute("tipos", TipoOcorrencia.values());
            req.getRequestDispatcher("/WEB-INF/views/frete/ocorrencia/form.jsp").forward(req, resp);
        }
    }

    private OcorrenciaFrete bind(HttpServletRequest req) {
        OcorrenciaFrete o = new OcorrenciaFrete();
        String idFrete = req.getParameter("idFrete");
        if (idFrete != null && !idFrete.isEmpty()) o.setIdFrete(Long.valueOf(idFrete));
        String tipo = req.getParameter("tipo");
        if (tipo != null && !tipo.isEmpty()) o.setTipo(TipoOcorrencia.valueOf(tipo));
        String dh = req.getParameter("dataHora");
        if (dh != null && !dh.isEmpty()) o.setDataHora(LocalDateTime.parse(dh));
        else o.setDataHora(LocalDateTime.now());
        o.setMunicipio(req.getParameter("municipio"));
        o.setUf(req.getParameter("uf") == null ? null : req.getParameter("uf").toUpperCase());
        o.setDescricao(req.getParameter("descricao"));
        o.setNomeRecebedor(req.getParameter("nomeRecebedor"));
        o.setDocumentoRecebedor(req.getParameter("documentoRecebedor"));
        return o;
    }
}
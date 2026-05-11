package br.com.nextlog.relatorio;

import br.com.nextlog.exception.NegocioException;
import br.com.nextlog.frete.Frete;
import br.com.nextlog.frete.FreteBO;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RelatorioBO {

    private static final Logger LOG = Logger.getLogger(RelatorioBO.class.getName());
    private static final DateTimeFormatter DATA_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final FreteBO freteBO = new FreteBO();

    public byte[] gerarFretesEmAbertoPdf() {
        try {
            List<Frete> fretes = freteBO.listarEmAberto();
            JasperReport report = compilar("/reports/fretes-em-aberto.jrxml");
            Map<String, Object> params = new HashMap<>();
            params.put("DATA_GERACAO", LocalDate.now().format(DATA_BR));
            JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(
                    fretes.isEmpty() ? Collections.emptyList() : fretes);
            JasperPrint print = JasperFillManager.fillReport(report, params, ds);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(print, out);
            return out.toByteArray();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Erro ao gerar relatório de fretes em aberto", e);
            throw new NegocioException("Erro ao gerar relatório.");
        }
    }

    public byte[] gerarRomaneioPdf(Long idMotorista, LocalDate data) {
        if (idMotorista == null) throw new NegocioException("Motorista é obrigatório.");
        if (data == null)        throw new NegocioException("Data é obrigatória.");
        try {
            List<Frete> fretes = freteBO.listarRomaneio(idMotorista, data);
            JasperReport report = compilar("/reports/romaneio-carga.jrxml");
            Map<String, Object> params = new HashMap<>();
            params.put("DATA_ROMANEIO",  data.format(DATA_BR));
            params.put("MOTORISTA_NOME", fretes.isEmpty() ? "-" : fretes.get(0).getMotoristaNome());
            params.put("VEICULO_PLACA",  fretes.isEmpty() ? "-" : fretes.get(0).getVeiculoPlaca());
            JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(
                    fretes.isEmpty() ? Collections.emptyList() : fretes);
            JasperPrint print = JasperFillManager.fillReport(report, params, ds);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(print, out);
            return out.toByteArray();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Erro ao gerar romaneio", e);
            throw new NegocioException("Erro ao gerar romaneio.");
        }
    }

    public String resumoMensal(int meses) {
        return freteBO.resumoMensalJson(meses);
    }

    private JasperReport compilar(String resource) throws Exception {
        try (InputStream in = getClass().getResourceAsStream(resource)) {
            if (in == null) throw new NegocioException("Arquivo de relatório não encontrado: " + resource);
            return JasperCompileManager.compileReport(in);
        }
    }
}
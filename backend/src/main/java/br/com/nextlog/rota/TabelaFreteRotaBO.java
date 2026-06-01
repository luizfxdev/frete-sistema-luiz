package br.com.nextlog.rota;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TabelaFreteRotaBO {

    private static final Logger LOG = Logger.getLogger(TabelaFreteRotaBO.class.getName());
    private final TabelaFreteRotaDAO tabelaFreteRotaDAO = new TabelaFreteRotaDAO();

    public TabelaFreteRota buscarRota(String municipioOrigem, String ufOrigem,
                                       String municipioDestino, String ufDestino) {
        try {
            return tabelaFreteRotaDAO.buscarRota(municipioOrigem, ufOrigem, municipioDestino, ufDestino);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao buscar rota", e);
            return null;
        }
    }

    public BigDecimal calcularValorSugerido(BigDecimal valorBase, BigDecimal valorPorKg, BigDecimal pesoKg) {
        if (valorBase == null) return BigDecimal.ZERO;
        if (pesoKg == null || pesoKg.compareTo(BigDecimal.ZERO) <= 0) return valorBase;

        BigDecimal acrescimo = valorPorKg.multiply(pesoKg).setScale(2, RoundingMode.HALF_UP);
        return valorBase.add(acrescimo).setScale(2, RoundingMode.HALF_UP);
    }
}
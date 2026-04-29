package br.com.nextlog.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class FreteNumberGenerator {

    private FreteNumberGenerator() {}

    public static String gerar(Connection conn) throws SQLException {
        int ano = LocalDate.now().getYear();
        String prefixo = "FRT-" + ano + "-";

        String sql = "SELECT numero FROM frete WHERE numero LIKE ? ORDER BY numero DESC LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, prefixo + "%");
            try (ResultSet rs = ps.executeQuery()) {
                int proximo = 1;
                if (rs.next()) {
                    String ultimo = rs.getString(1);
                    String sequencia = ultimo.substring(prefixo.length());
                    proximo = Integer.parseInt(sequencia) + 1;
                }
                return prefixo + String.format("%05d", proximo);
            }
        }
    }
}
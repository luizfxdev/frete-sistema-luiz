package br.com.nextlog.auth;

import br.com.nextlog.util.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAO {

    public Login buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT id, nome, email, senha, ativo FROM usuario WHERE email = ?";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                Login l = new Login();
                l.setId(rs.getLong("id"));
                l.setNome(rs.getString("nome"));
                l.setEmail(rs.getString("email"));
                l.setSenha(rs.getString("senha"));
                l.setAtivo(rs.getBoolean("ativo"));
                return l;
            }
        }
    }
}
package br.com.nextlog.util;

import org.apache.commons.dbcp.BasicDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ConnectionPool {

    private static final Logger LOG = Logger.getLogger(ConnectionPool.class.getName());
    private static BasicDataSource dataSource;

    private ConnectionPool() {}

    public static synchronized void inicializar() {
        if (dataSource != null) return;

        Properties props = new Properties();
        try (InputStream in = ConnectionPool.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new IllegalStateException("db.properties não encontrado no classpath. Copie db.properties.example.");
            }
            props.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Falha ao carregar db.properties", e);
        }

        dataSource = new BasicDataSource();
        dataSource.setDriverClassName(props.getProperty("db.driver", "org.postgresql.Driver"));
        dataSource.setUrl(props.getProperty("db.url"));
        dataSource.setUsername(props.getProperty("db.usuario"));
        dataSource.setPassword(props.getProperty("db.senha"));
        dataSource.setInitialSize(2);
        dataSource.setMaxActive(20);
        dataSource.setMaxIdle(10);
        dataSource.setMinIdle(2);
        dataSource.setMaxWait(10000);
        dataSource.setValidationQuery("SELECT 1");
        dataSource.setTestOnBorrow(true);

        LOG.info("Pool DBCP inicializado: " + dataSource.getUrl());
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) inicializar();
        return dataSource.getConnection();
    }

    public static synchronized void encerrar() {
        if (dataSource == null) return;
        try {
            dataSource.close();
            LOG.info("Pool DBCP encerrado.");
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "Erro ao encerrar pool DBCP", e);
        } finally {
            dataSource = null;
        }
    }
}
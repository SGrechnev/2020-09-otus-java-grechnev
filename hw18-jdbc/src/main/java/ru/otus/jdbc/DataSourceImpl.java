package ru.otus.jdbc;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public class DataSourceImpl implements DataSource {
    private static final String URL = "jdbc:postgresql://localhost:5432/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    @Override
    public Connection getConnection() throws SQLException {
        var connection = DriverManager.getConnection(URL, USER, PASSWORD);
        connection.setAutoCommit(false);
        return connection;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        var connection = DriverManager.getConnection(URL, username, password);
        connection.setAutoCommit(false);
        return connection;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new UnsupportedOperationException();
    }
}
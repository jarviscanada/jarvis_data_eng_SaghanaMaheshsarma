package ca.jrvs.apps.stockquote.cruddao.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManager {

  private static final String JDBC_URL_PREFIX = "jdbc:postgresql://";
  private static final String DEFAULT_HOST = "localhost";
  private static final int DEFAULT_PORT = 5432;

  private String host;
  private int port;
  private String databaseName;
  private String username;
  private String password;

  public DatabaseConnectionManager(String host, int port, String databaseName, String username, String password) {
    this.host = host;
    this.port = port;
    this.databaseName = databaseName;
    this.username = username;
    this.password = password;
  }

  public Connection getConnection() throws SQLException {
    String url = JDBC_URL_PREFIX + host + ":" + port + "/" + databaseName;
    return DriverManager.getConnection(url, username, password);
  }
}


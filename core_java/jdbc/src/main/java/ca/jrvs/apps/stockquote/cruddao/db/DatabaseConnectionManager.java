package ca.jrvs.apps.stockquote.cruddao.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManager {

  private static final String jdbc_url = "jdbc:postgresql://";
  private String host;
  private String port;
  private String databaseName;
  private String username;
  private String password;

  public DatabaseConnectionManager(String host, String port, String databaseName, String username, String password) {
    this.host = host;
    this.port = port;
    this.databaseName = databaseName;
    this.username = username;
    this.password = password;
  }

  public Connection getConnection() throws SQLException {
    String url = jdbc_url + host + ":" + Integer.parseInt(port) + "/" + databaseName;
    System.out.println(url);
    return DriverManager.getConnection(url, username, password);
  }
}


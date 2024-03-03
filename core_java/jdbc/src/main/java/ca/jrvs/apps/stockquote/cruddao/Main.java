package ca.jrvs.apps.stockquote.cruddao;

import ca.jrvs.apps.stockquote.cruddao.api.QuoteHttpHelper;
import ca.jrvs.apps.stockquote.cruddao.api.StockQuoteController;
import ca.jrvs.apps.stockquote.cruddao.dao.PositionDao;
import ca.jrvs.apps.stockquote.cruddao.dao.QuoteDao;
import ca.jrvs.apps.stockquote.cruddao.db.DatabaseConnectionManager;
import ca.jrvs.apps.stockquote.cruddao.service.PositionService;
import ca.jrvs.apps.stockquote.cruddao.service.QuoteService;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;

public class Main {
  private static final Logger flowLogger = AppLogger.getFlowLogger();
  private static final Logger errorLogger = AppLogger.getErrorLogger();

  public static void main(String[] args) {
    String propertiesFilePath = System.getenv("PROPERTIES_FILE_PATH");
    if (propertiesFilePath == null || propertiesFilePath.isEmpty()) {
      // Use default path when environment variable is not set
      propertiesFilePath = "src/main/resources/properties.txt";
    }
    Properties properties = loadProperties(propertiesFilePath);
    flowLogger.info("properties being accessed"+ properties);
    flowLogger.info("class property"+ properties.getProperty("db-class"));
    flowLogger.info("class property"+ properties.getProperty("username"));
    OkHttpClient client = new OkHttpClient();
    ObjectMapper mapper = new ObjectMapper();
    DatabaseConnectionManager jdbcDriver = createDatabaseConnectionManager(properties);

    try {
//      Class.forName(properties.getProperty("db-class"));
//      String url = "jdbc:postgresql://" + properties.getProperty("server") + ":" +
//          properties.getProperty("port") + "/" + properties.getProperty("database");
      String url = "jdbc:postgresql://localhost:5432/stock_quote";

      try (Connection c = DriverManager.getConnection(url, "saghanamaheshsarma","")) {
        QuoteDao qRepo = new QuoteDao(c);
        PositionDao pRepo = new PositionDao(c);
        QuoteHttpHelper rcon = new QuoteHttpHelper(properties.getProperty("api-key"),client,mapper,jdbcDriver);
        QuoteService sQuote = new QuoteService(qRepo, rcon);
        PositionService sPos = new PositionService(pRepo);
        StockQuoteController con = new StockQuoteController(sQuote, sPos,rcon);
        con.initClient();
      }
    } catch (SQLException e) {
      System.out.println("Error in connecting to the database"+e);
      errorLogger.error("Error occurred", e);
    }
  }

  private static Properties loadProperties(String filePath) {
    Properties properties = new Properties();
    try (FileInputStream fis = new FileInputStream(filePath)) {
      properties.load(fis);
    } catch (IOException e) {
      errorLogger.error("Error occurred at loading properties", e);
    }
    return properties;
  }

  private static DatabaseConnectionManager createDatabaseConnectionManager(Properties properties) {
    String host = properties.getProperty("server");
//    int port = Integer.parseInt(properties.getProperty("port"));
    String databaseName = properties.getProperty("database");
    String username = properties.getProperty("username");
    String password = properties.getProperty("password");
    return new DatabaseConnectionManager(host, 5432, databaseName, username, password);
  }
}



package ca.jrvs.apps.stockquote.cruddao.api;

import ca.jrvs.apps.stockquote.cruddao.db.DatabaseConnectionManager;
import ca.jrvs.apps.stockquote.cruddao.model.Quote;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.FileInputStream;
import java.sql.Connection;
import okhttp3.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class QuoteHttpHelper {

  private String apiKey;
  private OkHttpClient client;
  private ObjectMapper mapper;
  private DatabaseConnectionManager jdbcDriver;

  public QuoteHttpHelper(String apiKey, OkHttpClient client, ObjectMapper mapper, DatabaseConnectionManager jdbcDriver) {
    this.apiKey = apiKey;
    this.client = client;
    this.mapper = mapper;
    this.jdbcDriver = jdbcDriver;
  }

  private Properties loadProperties() throws IOException {
    Properties properties = new Properties();
    try (FileInputStream fis = new FileInputStream("src/resources/properties.txt")) {
      properties.load(fis);
    }
    return properties;
  }

  public Quote fetchQuoteInfo(String symbol) throws IllegalArgumentException, IOException, SQLException {

    HttpUrl.Builder urlBuilder = HttpUrl.parse("https://www.alphavantage.co/query").newBuilder();
    urlBuilder.addQueryParameter("function", "GLOBAL_QUOTE");
    urlBuilder.addQueryParameter("symbol", symbol);
    urlBuilder.addQueryParameter("apikey", apiKey);

    String url = urlBuilder.build().toString();

    Request request = new Request.Builder()
        .url(url)
        .build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new IOException("Unexpected code " + response);
      }

      Quote quote = parseQuote(response.body().string());
      quote.setTimestamp(new Timestamp(System.currentTimeMillis()));

      // Store quote in database
      storeQuoteInDatabase(quote);
      return quote;
    } catch (IOException e) {
      e.printStackTrace();
      throw new IllegalArgumentException("Error fetching quote data for symbol: " + symbol);
    }
  }

  private Quote parseQuote(String responseBody) throws IOException {
    System.out.println(responseBody);
    JsonNode rootNode = mapper.readTree(responseBody).path("Global Quote");
    Quote quote = new Quote();
    System.out.println("Parsing");
    quote.setTicker(rootNode.get("01. symbol").asText());
    System.out.println("Parsing");
    quote.setOpen(rootNode.get("02. open").asDouble());
    quote.setHigh(rootNode.get("03. high").asDouble());
    quote.setLow(rootNode.get("04. low").asDouble());
    quote.setPrice(rootNode.get("05. price").asDouble());
    quote.setVolume(rootNode.get("06. volume").asInt());
    quote.setLatestTradingDay(Date.valueOf(rootNode.get("07. latest trading day").asText()));
    quote.setPreviousClose(rootNode.get("08. previous close").asDouble());
    quote.setChange(rootNode.get("09. change").asDouble());
    quote.setChangePercent(rootNode.get("10. change percent").asText());
    System.out.println("Parsing");
    return quote;
  }


  private void storeQuoteInDatabase(Quote quote) throws SQLException {
    try (Connection connection = jdbcDriver.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          "INSERT INTO quote (symbol, open, high, low, price, volume, latest_trading_day, " +
              "previous_close, change, change_percent, timestamp) " +
              "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
      );
      statement.setString(1, quote.getTicker());
      statement.setDouble(2, quote.getOpen());
      statement.setDouble(3, quote.getHigh());
      statement.setDouble(4, quote.getLow());
      statement.setDouble(5, quote.getPrice());
      statement.setInt(6, quote.getVolume());
      statement.setDate(7, new java.sql.Date(quote.getLatestTradingDay().getTime()));
      statement.setDouble(8, quote.getPreviousClose());
      statement.setDouble(9, quote.getChange());
      statement.setString(10, quote.getChangePercent());
      statement.setTimestamp(11, quote.getTimestamp());

      statement.executeUpdate();
    }
  }
}

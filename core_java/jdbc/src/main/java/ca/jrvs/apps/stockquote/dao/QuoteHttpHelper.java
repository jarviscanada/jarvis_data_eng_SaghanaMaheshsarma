package ca.jrvs.apps.stockquote.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class QuoteHttpHelper {

  private final String apiKey;
  private final OkHttpClient client;
  private final ObjectMapper mapper;

  public QuoteHttpHelper(String apiKey) {
    this.apiKey = apiKey;
    this.client = new OkHttpClient();
    this.mapper = new ObjectMapper();
  }

  public QuoteHttpHelper(String apiKey, OkHttpClient client, ObjectMapper mapper) {
    this.apiKey = apiKey;
    this.client = client;
    this.mapper = mapper;
  }

  /**
   * Fetch latest quote data from Alpha Vantage endpoint
   *
   * @param symbol Ticker symbol for the stock
   * @return Quote with latest data
   * @throws IllegalArgumentException if no data was found for the given symbol
   */
  public Quote fetchQuoteInfo(String symbol) throws IllegalArgumentException {
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
      String responseBody = response.body().string();

      // Deserialize JSON response to Quote object
      Quote quote = mapper.readValue(responseBody, Quote.class);
      return quote;

    } catch (IOException e) {
      e.printStackTrace();
      throw new IllegalArgumentException("Error fetching quote data for symbol: " + symbol);
    }
  }

  public static void main(String[] args) {
    String apiKey = "api_key"; // Replace with your actual API key from Alpha Vantage
    QuoteHttpHelper httpHelper = new QuoteHttpHelper(apiKey);

    try {
      Quote quote = httpHelper.fetchQuoteInfo("MSFT");
      Quote.GlobalQuote globalQuote = quote.getGlobalQuote();
      System.out.println("Symbol: " + globalQuote.getSymbol());
      System.out.println("Open: " + globalQuote.getOpen());
      System.out.println("High: " + globalQuote.getHigh());
      System.out.println("Low: " + globalQuote.getLow());
      System.out.println("Price: " + globalQuote.getPrice());
      System.out.println("Volume: " + globalQuote.getVolume());
      System.out.println("Latest Trading Day: " + globalQuote.getLatestTradingDay());
      System.out.println("Previous Close: " + globalQuote.getPreviousClose());
      System.out.println("Change: " + globalQuote.getChange());
      System.out.println("Change Percent: " + globalQuote.getChangePercent());
      // Print other relevant fields...
    } catch (IllegalArgumentException e) {
      System.err.println(e.getMessage());
    }
  }
}


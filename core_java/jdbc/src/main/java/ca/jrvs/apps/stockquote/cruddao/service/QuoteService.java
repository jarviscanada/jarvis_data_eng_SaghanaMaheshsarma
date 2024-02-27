package ca.jrvs.apps.stockquote.cruddao.service;

import ca.jrvs.apps.stockquote.cruddao.AppLogger;
import ca.jrvs.apps.stockquote.cruddao.model.Quote;
import ca.jrvs.apps.stockquote.cruddao.dao.QuoteDao;
import ca.jrvs.apps.stockquote.cruddao.api.QuoteHttpHelper;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;
import java.util.List;
import org.slf4j.Logger;

public class QuoteService {

  private QuoteDao dao;
  private QuoteHttpHelper httpHelper;

  private List<String> validSymbols;
  private static final Logger flowLogger = AppLogger.getFlowLogger();
  private static final Logger errorLogger = AppLogger.getErrorLogger();

  // Constructor
  public QuoteService(QuoteDao dao, QuoteHttpHelper httpHelper) {
    this.dao = dao;
    this.httpHelper = httpHelper;
    this.validSymbols = Arrays.asList("AAPL", "MSFT", "GOOGL");
  }

  /**
   * Fetches latest quote data from endpoint
   * @param ticker
   * @return Latest quote information or empty optional if ticker symbol not found
   */
  public Optional<Quote> fetchQuoteDataFromAPI(String ticker) {
    try {
      if (!validSymbols.contains(ticker)) {
        throw new IllegalArgumentException("Invalid symbol: " + ticker);
      }
      return Optional.ofNullable(httpHelper.fetchQuoteInfo(ticker));
    } catch (IllegalArgumentException | IOException | SQLException e) {
      errorLogger.error("Error occurred", e);
      return Optional.empty();
    }
  }

  public Optional<Quote> getQuote(String ticker){
    return dao.findById(ticker);
  }

}


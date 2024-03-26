//package ca.jrvs.apps.stockquote.cruddao.service;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//import java.io.IOException;
//import java.sql.SQLException;
//import java.util.Optional;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import ca.jrvs.apps.stockquote.cruddao.api.QuoteHttpHelper;
//import ca.jrvs.apps.stockquote.cruddao.dao.QuoteDao;
//import ca.jrvs.apps.stockquote.cruddao.model.Quote;
//import ca.jrvs.apps.stockquote.cruddao.service.QuoteService;
//
//public class QuoteService_UnitTest {
//
//  private QuoteService quoteService;
//  private QuoteDao quoteDaoMock;
//  private QuoteHttpHelper quoteHttpHelperMock;
//
//  @BeforeEach
//  public void setup() {
//    quoteDaoMock = mock(QuoteDao.class);
//    quoteHttpHelperMock = mock(QuoteHttpHelper.class);
//    quoteService = new QuoteService(quoteDaoMock, quoteHttpHelperMock);
//  }
//
//  @Test
//  public void testFetchQuoteDataFromAPI_WithValidSymbol() throws IOException, SQLException {
//    String validTicker = "AAPL";
//    Quote expectedQuote = new Quote();
//
//    when(quoteHttpHelperMock.fetchQuoteInfo(validTicker)).thenReturn(expectedQuote);
//
//    assertTrue(quoteService.fetchQuoteDataFromAPI(validTicker).isPresent());
//  }
//
//  @Test
//  public void testFetchQuoteDataFromAPI_WithInvalidSymbol() throws IOException, SQLException {
//    String invalidTicker = "INVALID";
//
//    assertFalse(quoteService.fetchQuoteDataFromAPI(invalidTicker).isPresent());
//  }
//
//  @Test
//  public void testFetchQuoteDataFromAPI_WithIOException() throws IOException, SQLException {
//    String ticker = "AAPL";
//
//    // Simulate IOException when fetching quote data
//    when(quoteHttpHelperMock.fetchQuoteInfo(ticker)).thenThrow(IOException.class);
//
//    assertFalse(quoteService.fetchQuoteDataFromAPI(ticker).isPresent());
//  }
//
//  @Test
//  public void testFetchQuoteDataFromAPI_WithSQLException() throws IOException, SQLException {
//    String ticker = "AAPL";
//
//    when(quoteHttpHelperMock.fetchQuoteInfo(ticker)).thenThrow(SQLException.class);
//
//    assertFalse(quoteService.fetchQuoteDataFromAPI(ticker).isPresent());
//  }
//
//}

package ca.jrvs.apps.stockquote.cruddao.service;

import ca.jrvs.apps.stockquote.cruddao.api.QuoteHttpHelper;
import ca.jrvs.apps.stockquote.cruddao.dao.QuoteDao;
import ca.jrvs.apps.stockquote.cruddao.model.Quote;
import ca.jrvs.apps.stockquote.cruddao.service.QuoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class QuoteService_IntTest {

  @Mock
  private QuoteDao quoteDao;

  @Mock
  private QuoteHttpHelper quoteHttpHelper;

  @InjectMocks
  private QuoteService quoteService;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testFetchQuoteDataFromAPI_WithValidTicker() throws IOException, SQLException {
    // Mock data
    String validTicker = "AAPL";
    Quote mockQuote = new Quote();
    mockQuote.setTicker(validTicker);
    mockQuote.setOpen(145.0);
    mockQuote.setHigh(150.0);
    mockQuote.setLow(140.0);
    mockQuote.setPrice(147.0);
    mockQuote.setVolume(1000000);
    mockQuote.setLatestTradingDay(new Date());
    mockQuote.setPreviousClose(146.0);
    mockQuote.setChange(1.0);
    mockQuote.setChangePercent("0.7%");
    mockQuote.setTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));

    // Mock behavior of HTTP helper to return a quote
    when(quoteHttpHelper.fetchQuoteInfo(validTicker)).thenReturn(mockQuote);

    // Perform fetch quote data from API
    Optional<Quote> quoteOptional = quoteService.fetchQuoteDataFromAPI(validTicker);

    // Verify that the quote was fetched
    assertTrue(quoteOptional.isPresent());
    Quote fetchedQuote = quoteOptional.get();
    assertEquals(validTicker, fetchedQuote.getTicker());
    assertEquals(145.0, fetchedQuote.getOpen());
    assertEquals(150.0, fetchedQuote.getHigh());
    assertEquals(140.0, fetchedQuote.getLow());
    assertEquals(147.0, fetchedQuote.getPrice());
    assertEquals(1000000, fetchedQuote.getVolume());
    assertNotNull(fetchedQuote.getLatestTradingDay());
    assertEquals(146.0, fetchedQuote.getPreviousClose());
    assertEquals(1.0, fetchedQuote.getChange());
    assertEquals("0.7%", fetchedQuote.getChangePercent());
    assertNotNull(fetchedQuote.getTimestamp());
  }

  @Test
  public void testFetchQuoteDataFromAPI_WithInvalidTicker() throws IOException, SQLException {
    // Mock data
    String invalidTicker = "INVALID";

    // Mock behavior of HTTP helper to return null for an invalid ticker
    when(quoteHttpHelper.fetchQuoteInfo(invalidTicker)).thenReturn(null);

    // Perform fetch quote data from API
    Optional<Quote> quoteOptional = quoteService.fetchQuoteDataFromAPI(invalidTicker);

    // Verify that no quote was fetched for an invalid ticker
    assertFalse(quoteOptional.isPresent());
  }
}



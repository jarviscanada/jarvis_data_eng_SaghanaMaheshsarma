package ca.jrvs.apps.stockquote.cruddao.dao;

import ca.jrvs.apps.stockquote.cruddao.model.Quote;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class QuoteDaoTest {

  @Mock
  private Connection mockConnection;

  @Mock
  private PreparedStatement mockPreparedStatement;

  @Mock
  private ResultSet mockResultSet;

  private QuoteDao quoteDao;

  @BeforeEach
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    quoteDao = new QuoteDao(mockConnection);
  }

  @Test
  public void save() throws SQLException {
    Quote quote = new Quote();
    quote.setTicker("AAPL");
    quote.setOpen(100.0);
    quote.setHigh(110.0);
    quote.setLow(90.0);
    quote.setPrice(105.0);
    quote.setVolume(1000000);
    quote.setLatestTradingDay(new java.sql.Date(System.currentTimeMillis()));
    quote.setPreviousClose(100.0);
    quote.setChange(5.0);
    quote.setChangePercent("5%");
    quote.setTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));

    // Set other fields

    when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    when(mockPreparedStatement.executeUpdate()).thenReturn(1);

    Quote savedQuote = quoteDao.save(quote);
    assertEquals(quote, savedQuote);

    verify(mockConnection).prepareStatement(anyString());
    verify(mockPreparedStatement).executeUpdate();
  }

  @Test
  public void findById() throws SQLException {
    String symbol = "AAPL";

    when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    when(mockResultSet.next()).thenReturn(true);
    when(mockResultSet.getString("symbol")).thenReturn(symbol);
    // Set other fields

    Optional<Quote> foundQuote = quoteDao.findById(symbol);
    assertEquals(symbol, foundQuote.get().getTicker());

    verify(mockConnection).prepareStatement(anyString());
    verify(mockPreparedStatement).executeQuery();
    verify(mockResultSet).next();
    verify(mockResultSet).getString("symbol");
    // Verify other fields
  }

  @Test
  public void findAll() throws SQLException {
    String symbol1 = "AAPL";
    String symbol2 = "MSFT";

    when(mockConnection.createStatement()).thenReturn(mockPreparedStatement);
    when(mockPreparedStatement.executeQuery(anyString())).thenReturn(mockResultSet);
    when(mockResultSet.next()).thenReturn(true, true, false);
    when(mockResultSet.getString("symbol")).thenReturn(symbol1, symbol2);
    // Set other fields

    Iterable<Quote> quotes = quoteDao.findAll();
    List<Quote> quoteList = new ArrayList<>();
    quotes.forEach(quoteList::add);

    assertEquals(2, quoteList.size());
    assertEquals(symbol1, quoteList.get(0).getTicker());
    // Verify other fields
  }

  @Test
  public void deleteById() throws SQLException {
    String symbol = "AAPL";
    when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

    quoteDao.deleteById(symbol);

    verify(mockConnection).prepareStatement(anyString());
    verify(mockPreparedStatement).executeUpdate();
  }

  @Test
  public void deleteAll() throws SQLException {
    when(mockConnection.createStatement()).thenReturn(mockPreparedStatement);

    quoteDao.deleteAll();

    verify(mockConnection).createStatement();
    verify(mockPreparedStatement).executeUpdate(anyString());
  }
}

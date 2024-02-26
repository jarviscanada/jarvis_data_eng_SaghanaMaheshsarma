//package ca.jrvs.apps.stockquote.cruddao;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import ca.jrvs.apps.stockquote.cruddao.api.QuoteHttpHelper;
//import ca.jrvs.apps.stockquote.cruddao.model.Quote;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import okhttp3.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import java.io.IOException;
//import java.sql.SQLException;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//public class QuoteHttpHelperTest {
//
//  private OkHttpClient client;
//  private ObjectMapper mapper;
//  private QuoteHttpHelper quoteHttpHelper;
//
//  @BeforeEach
//  public void setUp() {
//    client = mock(OkHttpClient.class);
//    mapper = mock(ObjectMapper.class);
//    quoteHttpHelper = new QuoteHttpHelper("test_api_key", client, mapper, "jdbc:postgresql://localhost:5432/testdb");
//  }
//
//  @Test
//  public void testFetchQuoteInfo_Success() throws IOException, SQLException {
//    // Mock successful response from API
//    ResponseBody responseBody = ResponseBody.create(MediaType.parse("application/json"),
//        "{\"ticker\":\"MSFT\",\"open\":151.65,\"high\":153.42,\"low\":151.02,\"price\":152.06,\"volume\":9425575,\"latestTradingDay\":\"2019-12-12\",\"previousClose\":151.70,\"change\":0.36,\"changePercent\":\"0.2373%\"}");
//    Response successfulResponse = new Response.Builder()
//        .request(new Request.Builder().url("https://api.example.com/quote?symbol=MSFT&apikey=test_api_key").build())
//        .protocol(Protocol.HTTP_1_1)
//        .code(200)
//        .message("OK")
//        .body(responseBody)
//        .build();
//    when(client.newCall(any(Request.class)).execute()).thenReturn(successfulResponse);
//
//    // Mock Quote object creation from JSON
//    Quote expectedQuote = new Quote();
//    expectedQuote.setTicker("MSFT");
//    expectedQuote.setOpen(151.65);
//    expectedQuote.setHigh(153.42);
//    expectedQuote.setLow(151.02);
//    expectedQuote.setPrice(152.06);
//    expectedQuote.setVolume(9425575);
//    expectedQuote.setLatestTradingDay(new java.util.Date(119, 11, 12)); // Year is 1900-based in java.util.Date
//    expectedQuote.setPreviousClose(151.70);
//    expectedQuote.setChange(0.36);
//    expectedQuote.setChangePercent("0.2373%");
//
//    when(mapper.readValue(any(String.class), eq(Quote.class))).thenReturn(expectedQuote);
//
//    // Call the method under test
//    Quote actualQuote = quoteHttpHelper.fetchQuoteInfo("MSFT");
//
//    // Verify interactions
//    verify(client).newCall(any(Request.class));
//    verify(mapper).readValue(any(String.class), eq(Quote.class));
//
//    // Assert the result
//    assertEquals(expectedQuote, actualQuote);
//  }
//
//  @Test
//  public void testFetchQuoteInfo_UnsuccessfulResponse() throws IOException, SQLException {
//    // Mock unsuccessful response from API
//    Response unsuccessfulResponse = new Response.Builder()
//        .request(new Request.Builder().url("https://api.example.com/quote?symbol=MSFT&apikey=test_api_key").build())
//        .protocol(Protocol.HTTP_1_1)
//        .code(404)
//        .message("Not Found")
//        .build();
//    when(client.newCall(any(Request.class)).execute()).thenReturn(unsuccessfulResponse);
//
//    // Call the method under test and expect an IOException
//    assertThrows(IOException.class, () -> quoteHttpHelper.fetchQuoteInfo("MSFT"));
//
//    // Verify interactions
//    verify(client).newCall(any(Request.class));
//    verifyNoInteractions(mapper); // Ensure that mapper is not invoked when response is unsuccessful
//  }
//
//  @Test
//  public void testFetchQuoteInfo_IOException() throws IOException, SQLException {
//    // Mock IOException when making HTTP call
//    when(client.newCall(any(Request.class)).execute()).thenThrow(new IOException("Test IOException"));
//
//    // Call the method under test and expect an IOException
//    assertThrows(IOException.class, () -> quoteHttpHelper.fetchQuoteInfo("MSFT"));
//
//    // Verify interactions
//    verify(client).newCall(any(Request.class));
//    verifyNoInteractions(mapper); // Ensure that mapper is not invoked when an IOException occurs
//  }
//}

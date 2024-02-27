package ca.jrvs.apps.stockquote.cruddao.api;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.stockquote.cruddao.db.DatabaseConnectionManager;
import ca.jrvs.apps.stockquote.cruddao.model.Quote;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuoteHttpHelperTest {

  @Mock
  private OkHttpClient mockClient;

  @Mock
  private ObjectMapper mockMapper;

  @Mock
  private DatabaseConnectionManager mockDbManager;

  @InjectMocks
  private QuoteHttpHelper quoteHttpHelper;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    mockClient = mock(OkHttpClient.class);
    mockMapper = mock(ObjectMapper.class);
    quoteHttpHelper = new QuoteHttpHelper("OVSW83YP2Z4HEWMS OVSW83YP2Z4HEWMS", mockClient, mockMapper, mockDbManager);
  }

  @Test
  public void testFetchQuoteInfo_Success() throws IOException, SQLException {
    // Mock response body
//    String responseBody = "{\"Global Quote\": {\"01. symbol\": \"AAPL\",\"02. open\": \"332.3800\",\"03. high\": \"333.8300\",\"04. low\": \"326.3600\",\"05. price\": \"327.7300\",\"06. volume\": \"21085695\",\"07. latest trading day\": \"2023-10-13\",\"08. previous close\": \"331.1600\",\"09. change\": \"-3.4300\",\"10. change percent\": \"-1.0358%\"}}";

    String responseBody = "{\n" +
        "    \"Global Quote\": {\n" +
        "        \"01. symbol\": \"AAPL\",\n" +
        "        \"02. open\": \"182.2400\",\n" +
        "        \"03. high\": \"182.7600\",\n" +
        "        \"04. low\": \"180.6500\",\n" +
        "        \"05. price\": \"181.1600\",\n" +
        "        \"06. volume\": \"40715100\",\n" +
        "        \"07. latest trading day\": \"2024-02-26\",\n" +
        "        \"08. previous close\": \"182.5200\",\n" +
        "        \"09. change\": \"-1.3600\",\n" +
        "        \"10. change percent\": \"-0.7451%\"\n" +
        "    }\n" +
        "}";
    // Mock response
    Response mockResponse = mock(Response.class);
    when(mockResponse.isSuccessful()).thenReturn(true);
    when(mockResponse.body()).thenReturn(ResponseBody.create(responseBody, null));

    // Mock call
    Call mockCall = mock(Call.class);
    when(mockCall.execute()).thenReturn(mockResponse);

    // Mock client
    OkHttpClient mockClient = mock(OkHttpClient.class);
    when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);

    // Mock ObjectMapper
    ObjectMapper mockMapper = mock(ObjectMapper.class);
    when(mockMapper.readTree(anyString())).thenReturn(mock(JsonNode.class));

    // Mock DatabaseConnectionManager
    DatabaseConnectionManager mockConnectionManager = mock(DatabaseConnectionManager.class);
    Connection mockConnection = mock(Connection.class);
    when(mockConnectionManager.getConnection()).thenReturn(mockConnection);

    // Create QuoteHttpHelper instance
    QuoteHttpHelper quoteHttpHelper = new QuoteHttpHelper("OVSW83YP2Z4HEWMS OVSW83YP2Z4HEWMS", mockClient, mockMapper, mockConnectionManager);

    // Test and verify
    assertDoesNotThrow(() -> {
      Quote quote = quoteHttpHelper.fetchQuoteInfo("AAPL");
      System.out.println("Helloooo"+quote);
      assertNotNull(quote);
      assertEquals("AAPL", quote.getTicker());
      assertEquals(181.1600, quote.getPrice());
    });
  }

  @Test
  public void fetchQuoteInfo_UnsuccessfulResponse() throws IOException, SQLException {
    // Mock response
    Response mockResponse = mock(Response.class);
    when(mockResponse.isSuccessful()).thenReturn(false);

    // Mock call
    Call mockCall = mock(Call.class);
    when(mockCall.execute()).thenReturn(mockResponse);

    // Mock client
    when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);

    // Test and verify
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      quoteHttpHelper.fetchQuoteInfo("AAPL");
    });
    System.out.println("Actual error message: " + exception.getMessage()); // Print actual error message

    assertTrue(exception.getMessage().contains("Error fetching quote data"));
  }

  @Test
  public void fetchQuoteInfo_ExceptionThrown() throws SQLException,IOException {
    Call mockCall = mock(Call.class);
    when(mockCall.execute()).thenThrow(new IOException());

    // Stub the behavior of client.newCall() to return the custom mock
    when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);

    // Test and verify
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      quoteHttpHelper.fetchQuoteInfo("AAPL");
    });
    assertEquals("Error fetching quote data for symbol: AAPL", exception.getMessage());
  }

}

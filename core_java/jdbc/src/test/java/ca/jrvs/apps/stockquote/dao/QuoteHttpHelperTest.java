package ca.jrvs.apps.stockquote.dao;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class QuoteHttpHelperTest {

  @Test
  public void testFetchQuoteInfo() throws IOException{
    // Mock OkHttpClient
    OkHttpClient client = Mockito.mock(OkHttpClient.class);
    // Mock ObjectMapper
    ObjectMapper mapper = Mockito.mock(ObjectMapper.class);

    // Mock response
    String mockResponseBody = "{\"Global Quote\": {\"01. symbol\": \"MSFT\",\"02. open\": \"151.6500\",\"03. high\": \"153.4200\",\"04. low\": \"151.0200\",\"05. price\": \"152.0600\",\"06. volume\": \"9425575\",\"07. latest trading day\": \"2019-12-12\",\"08. previous close\": \"151.7000\",\"09. change\": \"0.3600\",\"10. change percent\": \"0.2373%\"}}";
    Response mockResponse = new Response.Builder()
        .request(new Request.Builder().url("http://test.com").build())
        .protocol(Protocol.HTTP_1_1)
        .code(200)
        .message("OK")
        .body(ResponseBody.create(MediaType.parse("application/json"), mockResponseBody))
        .build();

    try {
      // Mock OkHttpClient's newCall().execute() to return our mock response
      Call mockCall = Mockito.mock(Call.class);
      when(client.newCall(any(Request.class))).thenReturn(mockCall);
      when(mockCall.execute()).thenReturn(mockResponse);

      // Mock ObjectMapper's readValue() to return a Quote object
      Quote quote = new Quote();
      Quote.GlobalQuote globalQuote = new Quote.GlobalQuote();
      globalQuote.setSymbol("MSFT");
      globalQuote.setOpen("151.6500");
      globalQuote.setHigh("153.4200");
      globalQuote.setLow("151.0200");
      globalQuote.setPrice("152.0600");
      globalQuote.setVolume("9425575");
      globalQuote.setLatestTradingDay("2019-12-12");
      globalQuote.setPreviousClose("151.7000");
      globalQuote.setChange("0.3600");
      globalQuote.setChangePercent("0.2373%");
      quote.setGlobalQuote(globalQuote);
      when(mapper.readValue(mockResponseBody, Quote.class)).thenReturn(quote);

      // Create QuoteHttpHelper instance with mocked dependencies
      QuoteHttpHelper httpHelper = new QuoteHttpHelper("apikey", client, mapper);

      // Call the method to be tested
      Quote result = httpHelper.fetchQuoteInfo("MSFT");

      System.out.println(result.getGlobalQuote());
    ;  // Verify the result
      assertEquals("MSFT", result.getGlobalQuote().getSymbol());
      assertEquals("151.6500", result.getGlobalQuote().getOpen());
      assertEquals("153.4200", result.getGlobalQuote().getHigh());
      assertEquals("151.0200", result.getGlobalQuote().getLow());
      assertEquals("152.0600", result.getGlobalQuote().getPrice());
      assertEquals("9425575", result.getGlobalQuote().getVolume());
      assertEquals("2019-12-12", result.getGlobalQuote().getLatestTradingDay());
      assertEquals("151.7000", result.getGlobalQuote().getPreviousClose());
      assertEquals("0.3600", result.getGlobalQuote().getChange());
      assertEquals("0.2373%", result.getGlobalQuote().getChangePercent());

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

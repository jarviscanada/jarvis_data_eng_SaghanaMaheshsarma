package ca.jrvs.apps.stockquote.cruddao.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.jrvs.apps.stockquote.cruddao.dao.PositionDao;
import ca.jrvs.apps.stockquote.cruddao.model.Position;
import ca.jrvs.apps.stockquote.cruddao.service.PositionService;

public class PositionService_UnitTest {

  private PositionService positionService;
  private PositionDao positionDaoMock;

  @BeforeEach
  public void setup() {
    positionDaoMock = mock(PositionDao.class);
    positionService = new PositionService(positionDaoMock);
  }

  @Test
  public void testBuy_WithValidInputs() {
    String ticker = "AAPL";
    int numOfShares = 50;
    double pricePerShare = 200.0;

    // Mock an existing position
    Position existingPosition = new Position();
    existingPosition.setTicker(ticker);
    existingPosition.setNumOfShares(100);
    existingPosition.setValuePaid(5000.0); // Assuming previous value paid
    when(positionDaoMock.findById(ticker)).thenReturn(Optional.of(existingPosition));

    // Call the buy method
    Position boughtPosition = positionService.buy(ticker, numOfShares, pricePerShare);

    // Assertions
    assertEquals(ticker, boughtPosition.getTicker());
    assertEquals(150, boughtPosition.getNumOfShares()); // 50 + 100
    assertEquals(15000.0, boughtPosition.getValuePaid()); // 5000 + 50 * 200
  }

  @Test
  public void testBuy_CannotExceedAvailableVolume() {
    String ticker = "AAPL";
    int numOfShares = 100;
    double pricePerShare = 200.0;

    // Mock an existing position
    Position existingPosition = new Position();
    existingPosition.setTicker(ticker);
    existingPosition.setNumOfShares(50); // Available volume is 50
    existingPosition.setValuePaid(5000.0); // Assuming previous value paid
    when(positionDaoMock.findById(ticker)).thenReturn(Optional.of(existingPosition));

    // Call the buy method and assert that it throws an IllegalArgumentException
    assertThrows(IllegalArgumentException.class, () -> {
      positionService.buy(ticker, numOfShares, pricePerShare);
    });
  }

  @Test
  public void testSell_WithValidInputs() {
    String ticker = "AAPL";
    int numOfShares = 50;
    double pricePerShare = 250.0;

    // Mock an existing position
    Position existingPosition = new Position();
    existingPosition.setTicker(ticker);
    existingPosition.setNumOfShares(100);
    existingPosition.setValuePaid(10000.0); // Assuming previous value paid
    when(positionDaoMock.findById(ticker)).thenReturn(Optional.of(existingPosition));

    // Call the sell method
    Position soldPosition = positionService.sell(ticker, numOfShares, pricePerShare);

    // Assertions
    assertEquals(ticker, soldPosition.getTicker());
    assertEquals(50, soldPosition.getNumOfShares()); // 100 - 50
    assertEquals(-2500.0, soldPosition.getValuePaid()); // 10000 - 50 * 250
  }



}

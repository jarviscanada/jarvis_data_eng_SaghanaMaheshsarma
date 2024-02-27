package ca.jrvs.apps.stockquote.cruddao.service;

import ca.jrvs.apps.stockquote.cruddao.dao.PositionDao;
import ca.jrvs.apps.stockquote.cruddao.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PositionService_IntTest {

  @Mock
  private PositionDao positionDao;

  @InjectMocks
  private PositionService positionService;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testBuy_WithDatabaseInteraction() {
    // Prepare test data
    String validTicker = "AAPL";
    int initialShares = 100;
    double initialPricePerShare = 150.0;
    int buyShares = 50;
    double buyPricePerShare = 200.0;

    // Mock findById to return an initial position
    Position initialPosition = new Position();
    initialPosition.setTicker(validTicker);
    initialPosition.setNumOfShares(initialShares);
    initialPosition.setValuePaid(initialShares * initialPricePerShare);
    when(positionDao.findById(validTicker)).thenReturn(Optional.of(initialPosition));

    // Perform buy operation
    Position boughtPosition = positionService.buy(validTicker, buyShares, buyPricePerShare);
    assertNotNull(boughtPosition);
    assertEquals(initialShares + buyShares, boughtPosition.getNumOfShares());

    // Verify that findById was called once
    verify(positionDao, times(1)).findById(validTicker);
  }

  @Test
  public void testSell_WithDatabaseInteraction() {
    // Prepare test data
    String validTicker = "AAPL";
    int initialShares = 100;
    double initialPricePerShare = 150.0;
    int sellShares = 30;
    double sellPricePerShare = 250.0;

    // Mock findById to return an initial position
    Position initialPosition = new Position();
    initialPosition.setTicker(validTicker);
    initialPosition.setNumOfShares(initialShares);
    initialPosition.setValuePaid(initialShares * initialPricePerShare);
    when(positionDao.findById(validTicker)).thenReturn(Optional.of(initialPosition));

    // Perform sell operation
    Position soldPosition = positionService.sell(validTicker, sellShares, sellPricePerShare);
    assertNotNull(soldPosition);
    assertEquals(initialShares - sellShares, soldPosition.getNumOfShares());

    // Verify that findById was called once
    verify(positionDao, times(1)).findById(validTicker);
  }
}





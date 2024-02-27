package ca.jrvs.apps.stockquote.cruddao.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.jrvs.apps.stockquote.cruddao.dao.PositionDao;
import ca.jrvs.apps.stockquote.cruddao.model.Position;
import ca.jrvs.apps.stockquote.cruddao.service.PositionService;

public class PositionService_IntTest {

  private PositionService positionService;
  private PositionDao positionDaoMock;

  @BeforeEach
  public void setUp() {
    positionDaoMock = mock(PositionDao.class);
    positionService = new PositionService(positionDaoMock);
  }

  @Test
  public void testBuy() {
    // Mock existing position
    String ticker = "AAPL";
    Position existingPosition = new Position();
    existingPosition.setTicker(ticker);
    existingPosition.setNumOfShares(10);
    existingPosition.setValuePaid(2000.0);
    when(positionDaoMock.findById(ticker)).thenReturn(Optional.of(existingPosition));

    // Perform buy operation
    Position newPosition = positionService.buy(ticker, 5, 150.0);

    // Verify the updated position
    assertEquals(15, newPosition.getNumOfShares());
    assertEquals(2750.0, newPosition.getValuePaid());
  }

  @Test
  public void testSell() {
    // Mock existing position
    String ticker = "AAPL";
    Position existingPosition = new Position();
    existingPosition.setTicker(ticker);
    existingPosition.setNumOfShares(10);
    existingPosition.setValuePaid(2000.0);
    when(positionDaoMock.findById(ticker)).thenReturn(Optional.of(existingPosition));

    // Perform sell operation
    Position updatedPosition = positionService.sell(ticker, 3, 150.0);

    // Verify the updated position
    assertEquals(7, updatedPosition.getNumOfShares());
    assertEquals(1100.0, updatedPosition.getValuePaid());
  }
}




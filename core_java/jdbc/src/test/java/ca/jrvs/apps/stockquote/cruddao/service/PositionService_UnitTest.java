package ca.jrvs.apps.stockquote.cruddao.service;

import ca.jrvs.apps.stockquote.cruddao.dao.PositionDao;
import ca.jrvs.apps.stockquote.cruddao.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PositionService_UnitTest {

  private PositionDao positionDaoMock;
  private PositionService positionService;

  @BeforeEach
  void setUp() {
    positionDaoMock = Mockito.mock(PositionDao.class);
    positionService = new PositionService(positionDaoMock);
  }

  @Test
  void testBuy() {
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

    verify(positionDaoMock, times(1)).save(newPosition);
  }

  @Test
  void testSell() {
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



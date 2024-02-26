package ca.jrvs.apps.stockquote.cruddao.service;

import ca.jrvs.apps.stockquote.cruddao.dao.PositionDao;
import ca.jrvs.apps.stockquote.cruddao.model.Position;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PositionService_UnitTest {

  @Mock
  private PositionDao positionDao;

  @InjectMocks
  private PositionService positionService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testBuy_NewPosition() throws SQLException {
    // Mock the behavior of positionDao
    when(positionDao.findById("AAPL")).thenReturn(Optional.empty());

    // Call the method under test
    Position position = positionService.buy("AAPL", 100, 150.0);

    // Verify the result
    assertEquals("AAPL", position.getTicker());
    assertEquals(100, position.getNumOfShares());
    assertEquals(150.0 * 100, position.getValuePaid());
    // Add more assertions as needed
  }

  // Write more test methods to cover other scenarios
}

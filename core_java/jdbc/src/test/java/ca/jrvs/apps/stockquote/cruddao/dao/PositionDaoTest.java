package ca.jrvs.apps.stockquote.cruddao.dao;

import ca.jrvs.apps.stockquote.cruddao.model.Position;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class PositionDaoTest {

  @Mock
  private Connection mockConnection;

  @Mock
  private PreparedStatement mockPreparedStatement;

  @Mock
  private ResultSet mockResultSet;

  private PositionDao positionDao;

  @BeforeEach
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    positionDao = new PositionDao(mockConnection);
  }

  @Test
  public void save() throws SQLException {
    // Mock entity
    Position entity = new Position();
    entity.setTicker("AAPL");
    entity.setNumOfShares(100);
    entity.setValuePaid(5000.0);

    // Mock PreparedStatement behavior
    when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockPreparedStatement);
    when(mockPreparedStatement.executeUpdate()).thenReturn(1);

    // Call the method under test
    Position savedPosition = positionDao.save(entity);

    // Verify interactions
    verify(mockConnection, times(1)).prepareStatement(any(String.class));
    verify(mockPreparedStatement, times(1)).setString(1, entity.getTicker());
    verify(mockPreparedStatement, times(1)).setInt(2, entity.getNumOfShares());
    verify(mockPreparedStatement, times(1)).setDouble(3, entity.getValuePaid());
    verify(mockPreparedStatement, times(1)).executeUpdate();

    // Assert that the saved position is not null
    assertNotNull(savedPosition);
  }



  @Test
  public void findById() throws SQLException {
    String symbol = "AAPL";
    when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    when(mockResultSet.next()).thenReturn(true);
    when(mockResultSet.getString("symbol")).thenReturn(symbol);
    when(mockResultSet.getInt("number_of_shares")).thenReturn(100);
    when(mockResultSet.getDouble("value_paid")).thenReturn(5000.0);

    Optional<Position> foundPosition = positionDao.findById(symbol);
    assertEquals(symbol, foundPosition.get().getTicker());

    verify(mockConnection).prepareStatement(anyString());
    verify(mockPreparedStatement).setString(1, symbol);
    verify(mockPreparedStatement).executeQuery();
    verify(mockResultSet).next();
    verify(mockResultSet).getString("symbol");
    verify(mockResultSet).getInt("number_of_shares");
    verify(mockResultSet).getDouble("value_paid");
  }

  @Test
  public void findAll() throws SQLException {
    String symbol1 = "AAPL";
    String symbol2 = "MSFT";

    when(mockConnection.createStatement()).thenReturn(mockPreparedStatement);
    when(mockPreparedStatement.executeQuery(anyString())).thenReturn(mockResultSet);
    when(mockResultSet.next()).thenReturn(true, true, false);
    when(mockResultSet.getString("symbol")).thenReturn(symbol1, symbol2);
    when(mockResultSet.getInt("number_of_shares")).thenReturn(100, 200);
    when(mockResultSet.getDouble("value_paid")).thenReturn(5000.0, 10000.0);

    Iterable<Position> positions = positionDao.findAll();
    List<Position> positionList = new ArrayList<>();
    positions.forEach(positionList::add);

    assertEquals(2, positionList.size());
    assertEquals(symbol1, positionList.get(0).getTicker());
    assertEquals(100, positionList.get(0).getNumOfShares());
    assertEquals(5000.0, positionList.get(0).getValuePaid(), 0);
    assertEquals(symbol2, positionList.get(1).getTicker());
    assertEquals(200, positionList.get(1).getNumOfShares());
    assertEquals(10000.0, positionList.get(1).getValuePaid(), 0);

    verify(mockConnection).createStatement();
    verify(mockPreparedStatement).executeQuery(anyString());
    verify(mockResultSet, times(3)).next();
    verify(mockResultSet, times(2)).getString("symbol");
    verify(mockResultSet, times(2)).getInt("number_of_shares");
    verify(mockResultSet, times(2)).getDouble("value_paid");
  }

  @Test
  public void deleteById() throws SQLException {
    String symbol = "AAPL";
    when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

    positionDao.deleteById(symbol);

    verify(mockConnection).prepareStatement(anyString());
    verify(mockPreparedStatement).setString(1, symbol);
    verify(mockPreparedStatement).executeUpdate();
  }

  @Test
  public void deleteAll() throws SQLException {
    when(mockConnection.createStatement()).thenReturn(mockPreparedStatement);

    positionDao.deleteAll();

    verify(mockConnection).createStatement();
    verify(mockPreparedStatement).executeUpdate(anyString());
  }
}


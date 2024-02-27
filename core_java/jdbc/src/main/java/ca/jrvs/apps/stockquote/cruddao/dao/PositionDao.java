package ca.jrvs.apps.stockquote.cruddao.dao;

import ca.jrvs.apps.stockquote.cruddao.dao.CrudDao;
import ca.jrvs.apps.stockquote.cruddao.model.Position;
import java.sql.*;
import java.util.Optional;

import java.util.ArrayList;
import java.util.List;

public class PositionDao implements CrudDao<Position, String> {

  private Connection c;

  // Constructor
  public PositionDao(Connection c) {
    this.c = c;
  }

  @Override
  public Position save(Position entity) throws IllegalArgumentException {
    if (entity == null || entity.getTicker() == null) {
      throw new IllegalArgumentException("Entity or ID cannot be null");
    }

    try {
      PreparedStatement stmt = c.prepareStatement(
          "INSERT INTO position (symbol, number_of_shares, value_paid) VALUES (?, ?, ?) " +
              "ON CONFLICT (symbol) DO UPDATE SET number_of_shares = ?, value_paid = ?"
      );
      stmt.setString(1, entity.getTicker());
      stmt.setInt(2, entity.getNumOfShares());
      stmt.setDouble(3, entity.getValuePaid());
      stmt.setInt(4, entity.getNumOfShares());
      stmt.setDouble(5, entity.getValuePaid());
      stmt.executeUpdate();
      return entity;

    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public Optional<Position> findById(String id) throws IllegalArgumentException {
    if (id == null) {
      throw new IllegalArgumentException("ID cannot be null");
    }

    try {
      PreparedStatement stmt = c.prepareStatement("SELECT * FROM position WHERE symbol = ?");
      stmt.setString(1, id);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        Position position = new Position();
        position.setTicker(rs.getString("symbol"));
        position.setNumOfShares(rs.getInt("number_of_shares"));
        position.setValuePaid(rs.getDouble("value_paid"));
        return Optional.of(position);
      } else {
        return Optional.empty();
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }

  @Override
  public Iterable<Position> findAll() {
    List<Position> positions = new ArrayList<>();
    try {
      Statement stmt = c.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM position");

      while (rs.next()) {
        Position position = new Position();
        position.setTicker(rs.getString("symbol"));
        position.setNumOfShares(rs.getInt("number_of_shares"));
        position.setValuePaid(rs.getDouble("value_paid"));
        positions.add(position);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return positions;
  }

  @Override
  public void deleteById(String id) throws IllegalArgumentException {
    if (id == null) {
      throw new IllegalArgumentException("ID cannot be null");
    }

    try {
      PreparedStatement stmt = c.prepareStatement("DELETE FROM position WHERE symbol = ?");
      stmt.setString(1, id);
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void deleteAll() {
    try {
      Statement stmt = c.createStatement();
      stmt.executeUpdate("DELETE FROM position");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}



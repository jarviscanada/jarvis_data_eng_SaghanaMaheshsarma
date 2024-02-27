package ca.jrvs.apps.stockquote.cruddao.dao;

import ca.jrvs.apps.stockquote.cruddao.dao.CrudDao;
import ca.jrvs.apps.stockquote.cruddao.model.Quote;
import java.sql.*;
import java.util.Optional;

import java.util.ArrayList;
import java.util.List;

public class QuoteDao implements CrudDao<Quote, String> {

  private Connection c;

  // Constructor
  public QuoteDao(Connection c) {
    this.c = c;
  }

  @Override
  public Quote save(Quote entity) throws IllegalArgumentException {
    if (entity == null || entity.getTicker() == null) {
      throw new IllegalArgumentException("Entity or ID cannot be null");
    }

    try {
      PreparedStatement stmt = c.prepareStatement(
          "INSERT INTO quote (ticker, open, high, low, price, volume, latest_trading_day, " +
              "previous_close, change, change_percent, timestamp) " +
              "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
              "ON CONFLICT (ticker) DO UPDATE SET open = ?, high = ?, low = ?, price = ?, " +
              "volume = ?, latest_trading_day = ?, previous_close = ?, change = ?, " +
              "change_percent = ?, timestamp = ?"
      );
      stmt.setString(1, entity.getTicker());
      stmt.setDouble(2, entity.getOpen());
      stmt.setDouble(3, entity.getHigh());
      stmt.setDouble(4, entity.getLow());
      stmt.setDouble(5, entity.getPrice());
      stmt.setInt(6, entity.getVolume());
      stmt.setDate(7, new java.sql.Date(entity.getLatestTradingDay().getTime()));
      stmt.setDouble(8, entity.getPreviousClose());
      stmt.setDouble(9, entity.getChange());
      stmt.setString(10, entity.getChangePercent());
      stmt.setTimestamp(11, new Timestamp(entity.getTimestamp().getTime()));

      stmt.setDouble(12, entity.getOpen());
      stmt.setDouble(13, entity.getHigh());
      stmt.setDouble(14, entity.getLow());
      stmt.setDouble(15, entity.getPrice());
      stmt.setInt(16, entity.getVolume());
      stmt.setDate(17, new java.sql.Date(entity.getLatestTradingDay().getTime()));
      stmt.setDouble(18, entity.getPreviousClose());
      stmt.setDouble(19, entity.getChange());
      stmt.setString(20, entity.getChangePercent());
      stmt.setTimestamp(21, new Timestamp(entity.getTimestamp().getTime()));

      stmt.executeUpdate();
      return entity;

    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public Optional<Quote> findById(String id) throws IllegalArgumentException {
    if (id == null) {
      throw new IllegalArgumentException("ID cannot be null");
    }

    try {
      PreparedStatement stmt = c.prepareStatement("SELECT * FROM quote WHERE symbol = ?");
      stmt.setString(1, id);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        Quote quote = new Quote();
        quote.setTicker(rs.getString("symbol"));
        quote.setOpen(rs.getDouble("open"));
        quote.setHigh(rs.getDouble("high"));
        quote.setLow(rs.getDouble("low"));
        quote.setPrice(rs.getDouble("price"));
        quote.setVolume(rs.getInt("volume"));
        quote.setLatestTradingDay(rs.getDate("latest_trading_day"));
        quote.setPreviousClose(rs.getDouble("previous_close"));
        quote.setChange(rs.getDouble("change"));
        quote.setChangePercent(rs.getString("change_percent"));
        quote.setTimestamp(rs.getTimestamp("timestamp"));
        return Optional.of(quote);
      } else {
        return Optional.empty();
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }

  @Override
  public Iterable<Quote> findAll() {
    List<Quote> quotes = new ArrayList<>();
    try {
      Statement stmt = c.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM quote");

      while (rs.next()) {
        Quote quote = new Quote();
        quote.setTicker(rs.getString("symbol"));
        quote.setOpen(rs.getDouble("open"));
        quote.setHigh(rs.getDouble("high"));
        quote.setLow(rs.getDouble("low"));
        quote.setPrice(rs.getDouble("price"));
        quote.setVolume(rs.getInt("volume"));
        quote.setLatestTradingDay(rs.getDate("latest_trading_day"));
        quote.setPreviousClose(rs.getDouble("previous_close"));
        quote.setChange(rs.getDouble("change"));
        quote.setChangePercent(rs.getString("change_percent"));
        quote.setTimestamp(rs.getTimestamp("timestamp"));
        quotes.add(quote);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return quotes;
  }

  @Override
  public void deleteById(String id) throws IllegalArgumentException {
    if (id == null) {
      throw new IllegalArgumentException("ID cannot be null");
    }

    try {
      PreparedStatement stmt = c.prepareStatement("DELETE FROM quote WHERE symbol = ?");
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
      stmt.executeUpdate("DELETE FROM quote");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}



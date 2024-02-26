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
      PreparedStatement stmt = c.prepareStatement("REPLACE INTO quotes VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
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
      PreparedStatement stmt = c.prepareStatement("SELECT * FROM quotes WHERE ticker = ?");
      stmt.setString(1, id);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        Quote quote = new Quote();
        quote.setTicker(rs.getString("ticker"));
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
      ResultSet rs = stmt.executeQuery("SELECT * FROM quotes");

      while (rs.next()) {
        Quote quote = new Quote();
        quote.setTicker(rs.getString("ticker"));
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
      PreparedStatement stmt = c.prepareStatement("DELETE FROM quotes WHERE ticker = ?");
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
      stmt.executeUpdate("DELETE FROM quotes");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}



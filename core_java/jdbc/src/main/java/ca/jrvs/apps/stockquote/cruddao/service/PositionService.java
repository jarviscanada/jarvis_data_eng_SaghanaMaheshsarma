package ca.jrvs.apps.stockquote.cruddao.service;

import ca.jrvs.apps.stockquote.cruddao.AppLogger;
import ca.jrvs.apps.stockquote.cruddao.model.Position;
import ca.jrvs.apps.stockquote.cruddao.dao.PositionDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;

public class PositionService {

  private PositionDao positionDao;

  private static final Logger flowLogger = AppLogger.getFlowLogger();
  private static final Logger errorLogger = AppLogger.getErrorLogger();

  public PositionService(PositionDao positionDao) {
    this.positionDao = positionDao;
  }

  public Position buy(String ticker, int numOfShares, double pricePerShare) {
    // Retrieve the existing position for the given ticker
    Optional<Position> existingPositionOpt = positionDao.findById(ticker);
    Position newPosition;

    // Check if the position exists
    if (existingPositionOpt.isPresent()) {
      Position existingPosition = existingPositionOpt.get();
      int availableVolume = existingPosition.getNumOfShares();

      // Check if the requested number of shares to buy exceeds the available volume
      if (numOfShares > availableVolume) {
        throw new IllegalArgumentException("Cannot buy more shares than available volume");
      }

      // Update the existing position with the new number of shares and value paid
      existingPosition.setNumOfShares(existingPosition.getNumOfShares() + numOfShares);
      existingPosition.setValuePaid(existingPosition.getValuePaid() + (numOfShares * pricePerShare));
      newPosition = existingPosition;
    } else {
      // Create a new position if one doesn't already exist
      newPosition = new Position();
      newPosition.setTicker(ticker);
      newPosition.setNumOfShares(numOfShares);
      newPosition.setValuePaid(numOfShares * pricePerShare);
    }

    // Save or update the position in the database
    positionDao.save(newPosition);
    return newPosition;
  }

  public Position sell(String ticker, int numOfShares, double pricePerShare) {
    // Retrieve the existing position for the given ticker
    Optional<Position> existingPositionOpt = positionDao.findById(ticker);

    // Check if the position exists
    if (existingPositionOpt.isPresent()) {
      Position existingPosition = existingPositionOpt.get();
      int availableVolume = existingPosition.getNumOfShares();

      System.out.println(availableVolume+ " " + numOfShares);
      // Check if the requested number of shares to sell exceeds the available volume
      if (numOfShares > availableVolume) {
        throw new IllegalArgumentException("Cannot sell more shares than available volume");
      }

      // Update the existing position with the new number of shares and value paid
      existingPosition.setNumOfShares(existingPosition.getNumOfShares() - numOfShares);
      existingPosition.setValuePaid(existingPosition.getValuePaid() - (numOfShares * pricePerShare));

      // Save the updated position in the database
      positionDao.save(existingPosition);
      return existingPosition;
    } else {
      throw new IllegalArgumentException("No position found for ticker: " + ticker);
    }
  }
  public List<Position> getAllPositions(){
    Iterable<Position> positionsIterable = positionDao.findAll();
    List<Position> positions = new ArrayList<>();
    positionsIterable.forEach(positions::add);
    return positions;
  }
}



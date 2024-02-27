package ca.jrvs.apps.stockquote.cruddao.api;

import ca.jrvs.apps.stockquote.cruddao.model.Position;
import ca.jrvs.apps.stockquote.cruddao.model.Quote;
import ca.jrvs.apps.stockquote.cruddao.service.PositionService;
import ca.jrvs.apps.stockquote.cruddao.service.QuoteService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

public class StockQuoteController {

  private QuoteService quoteService;
  private PositionService positionService;

  private QuoteHttpHelper quoteHttpHelper;

  public StockQuoteController(QuoteService quoteService, PositionService positionService,QuoteHttpHelper quoteHttpHelper) {
    this.quoteService = quoteService;
    this.positionService = positionService;
    this.quoteHttpHelper = quoteHttpHelper;
  }

  /**
   * User interface for our application
   */
  public void initClient() {
    Scanner scanner = new Scanner(System.in);
    while (true) {
      displayMenu();
      int choice = readIntegerInput(scanner);

      switch (choice) {
        case 1:
          buyStock(scanner);
          break;
        case 2:
          sellStock(scanner);
          break;
        case 3:
          viewPortfolio();
          break;
        case 4:
          viewQuote(scanner);
          break;
        case 5:
          fetchAndStoreQuote(scanner);
          break;
        case 6:
          System.out.println("Exiting application...");
          return;
        default:
          System.out.println("Invalid choice. Please enter a number between 1 and 5.");
      }
    }
  }

  private void displayMenu() {
    System.out.println("\n===== Stock Quote Application =====");
    System.out.println("1. Buy Stock");
    System.out.println("2. Sell Stock");
    System.out.println("3. View Portfolio");
    System.out.println("4. View Quote");
    System.out.println("5. Fetch and store Quote");
    System.out.println("6. Exit");
    System.out.print("Enter your choice: ");
  }

  private int readIntegerInput(Scanner scanner) {
    try {
      return scanner.nextInt();
    } catch (InputMismatchException e) {
      scanner.next(); // Consume invalid input
      return -1;
    }
  }

  private void buyStock(Scanner scanner) {
    System.out.print("Enter the stock symbol to buy: ");
    String symbol = scanner.next().toUpperCase();

    System.out.print("Enter the number of shares to buy: ");
    int numOfShares = readIntegerInput(scanner);
    if (numOfShares <= 0) {
      System.out.println("Invalid number of shares.");
      return;
    }

    System.out.print("Enter the price per share: ");
    double pricePerShare = scanner.nextDouble();

    positionService.buy(symbol, numOfShares, pricePerShare);
    System.out.println("Stock bought successfully.");
  }

  private void sellStock(Scanner scanner) {
    System.out.print("Enter the stock symbol to sell: ");
    String symbol = scanner.next().toUpperCase();

    System.out.print("Enter the number of shares to sell: ");
    int numOfShares = readIntegerInput(scanner);
    if (numOfShares <= 0) {
      System.out.println("Invalid number of shares.");
      return;
    }

    System.out.print("Enter the price per share: ");
    double pricePerShare = scanner.nextDouble();

    positionService.sell(symbol, numOfShares, pricePerShare);
    System.out.println("Stock sold successfully.");
  }

  private void viewPortfolio() {
    System.out.println("===== Portfolio =====");
    for (Position position : positionService.getAllPositions()) {
      System.out.println(position);
    }
  }

  private void viewQuote(Scanner scanner) {
    System.out.print("Enter the stock symbol to view quote: ");
    String symbol = scanner.next().toUpperCase();

    Optional<Quote> quote = quoteService.getQuote(symbol);
    if (quote.isPresent()) {
      System.out.println("===== Quote for " + symbol + " =====");
      System.out.println(quote.get());
    } else {
      System.out.println("Quote not found for symbol: " + symbol);
    }
  }

  private void fetchAndStoreQuote(Scanner scanner) {
    System.out.print("Enter the stock symbol to fetch and store quote: ");
    String symbol = scanner.next().toUpperCase();

    try {
      Quote quote = quoteHttpHelper.fetchQuoteInfo(symbol);
      System.out.print("Quote fetched and stored successfully");
    } catch (IllegalArgumentException | IOException | SQLException e) {
      e.printStackTrace();
      System.out.println("Error fetching and storing quote.");
    }
  }
}


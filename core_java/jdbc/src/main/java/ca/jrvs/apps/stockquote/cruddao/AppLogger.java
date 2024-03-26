package ca.jrvs.apps.stockquote.cruddao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppLogger {
  private static final Logger flowLogger = (Logger) LoggerFactory.getLogger("com.example.flow");
  private static final Logger errorLogger = (Logger) LoggerFactory.getLogger("com.example.error");

  public static Logger getFlowLogger() {
    return flowLogger;
  }

  public static Logger getErrorLogger() {
    return errorLogger;
  }
}



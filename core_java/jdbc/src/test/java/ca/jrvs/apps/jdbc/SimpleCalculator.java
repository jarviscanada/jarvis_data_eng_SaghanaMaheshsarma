package ca.jrvs.apps.jdbc;

public interface SimpleCalculator {
  int add (int x,int y);
  int subtract(int x, int y);
  int multiply(int x,int y);
  double divide(int x, int y);

  int power(int x , int y);
  int abs(int x);
  double sqrt(int x);

}

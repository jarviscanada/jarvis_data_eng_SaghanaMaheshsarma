package ca.jrvs.apps.jdbc;

public class SimpleCalculatorImpl implements SimpleCalculator{


  public int add(int x, int y) {
    return x + y;
  }


  public int subtract(int x, int y) {
    return x - y;
  }


  public int multiply(int x, int y) {
    return x * y;
  }


  public double divide(int x, int y) {
    if(y==0){
      throw new ArithmeticException("Cannot divide by zero");
    }
    return (double) x / y;
  }


  public int power(int x, int y) {
    return (int) Math.pow(x,y);
  }


  public int abs(int x) {
    return Math.abs(x);
  }


  public double sqrt(int x) {
    if(x<0){
      throw new IllegalArgumentException("Cannot calculate square root of zero");
    }
    return Math.sqrt(x);
  }

}

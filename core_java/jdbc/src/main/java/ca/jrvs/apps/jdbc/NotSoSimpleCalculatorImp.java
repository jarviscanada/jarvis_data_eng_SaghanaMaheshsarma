package ca.jrvs.apps.jdbc;

public class NotSoSimpleCalculatorImp implements NotSoSimpleCalculator{

  private SimpleCalculator calc;

  public NotSoSimpleCalculatorImp(SimpleCalculator calc){
    this.calc =calc;
  }

  @Override
  public int power(int x, int y) {
   return calc.power(x,y);
  }

  @Override
  public int abs(int x) {
    return calc.multiply(x,-1);
  }

  @Override
  public double sqrt(int x) {

    return calc.sqrt(x);
  }
}

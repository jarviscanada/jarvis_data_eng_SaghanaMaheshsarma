package ca.jrvs.apps.jdbc;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SimpleCalculatorImplTest {
  SimpleCalculator calculator;
  @BeforeEach
  void init(){
    calculator = new SimpleCalculatorImpl();
  }

  @Test
  void test_add(){
    int expected = 2;
    int actual = calculator.add(1,1);
    Assertions.assertEquals(expected,actual);
  }

  @Test
  void testSubtract(){
    Assertions.assertEquals(4,calculator.subtract(5,1));
  }

  @Test
  void testMultiply(){
    Assertions.assertEquals(15, calculator.multiply(3,5));
  }

  @Test
  void testDivide(){
    Assertions.assertEquals(2.0, calculator.divide(6,3),0.0001);
  }

  @Test
  void testDivideByZero(){
    Assertions.assertThrows(ArithmeticException.class, () ->calculator.divide(5,0));
  }



}
package ca.jrvs.apps.jdbc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotSoSimpleCalculatorImpTest {

  NotSoSimpleCalculator calc;

  @Mock
  SimpleCalculator mockSimpleCalc;

  @BeforeEach
  void init(){
    calc = new NotSoSimpleCalculatorImp(mockSimpleCalc);
  }

  @Test
  void test_power() {
    // Stubbing - Define behavior for the method call
    when(mockSimpleCalc.power(2, 3)).thenReturn(8);

    // Perform the test
    int result = calc.power(2, 3);

    // Verify the interaction (optional)
    verify(mockSimpleCalc).power(2, 3);

    // Assertion
    assertEquals(8, result);
  }

  @Test
  void test_abs() {
    // Stubbing - Define behavior for the method call
    when(mockSimpleCalc.multiply(10,-1)).thenReturn(-10);

    // Perform the test
    int result = calc.abs(10);

    // Verify the interaction (optional)
    verify(mockSimpleCalc).multiply(10,-1);

    // Assertion
    assertEquals(-10, result);
  }

  @Test
  void test_sqrt() {
    // Stubbing - Define behavior for the method call
    when(mockSimpleCalc.sqrt(25)).thenReturn(5.0);

    // Perform the test
    double result = calc.sqrt(25);

    // Verify the interaction (optional)
    verify(mockSimpleCalc).sqrt(25);

    // Assertion
    assertEquals(5.0, result, 0.0001);
  }
}
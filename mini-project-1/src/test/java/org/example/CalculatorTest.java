package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    /*
    * Basic and Edge unit cases
    *
    * */

    double calculatorHelper(String expression) {
        Calculator calculator = new Calculator();

        calculator.setExpression(expression);
        calculator.calculate();
        return Double.parseDouble(calculator.getAnswer());
    }

    double calculatorHelper(String expression, boolean isNaive) {
        Calculator calculator = null;
        calculator = isNaive ? new Calculator() : new CalculatorV2();

        calculator.setExpression(expression);
        calculator.calculate();
        return Double.parseDouble(calculator.getAnswer());
    }

    @Test
    void testCalculateAddition() {
        Assertions.assertEquals(19, calculatorHelper("10+9"));
    }

    @Test
    void testCalculateMultiplication() {
        Assertions.assertEquals(38700, calculatorHelper("300*129"));
    }

    @Test
    void testCalculateSubtraction() {
        Assertions.assertEquals(-72, calculatorHelper("72-144"));
    }

    @Test
    void testCalculateDivision() {
        Assertions.assertEquals(25, calculatorHelper("100/4"));
    }

    @Test
    void testCalculateExponent() {
        Assertions.assertEquals(32, calculatorHelper("2^5"));
    }

    @Test
    void testCalculateMultipleExpression() {
        Assertions.assertEquals(-105.6, calculatorHelper("10-120+11/3*1.2"));
    }

    @Test
    void testCalculateInfinity() {
        Assertions.assertEquals(Double.NEGATIVE_INFINITY, calculatorHelper("100+2-99.5/0^2+1-200"));
    }

    @Test
    void testCalculateNaN() {
        Assertions.assertEquals(Double.NaN, calculatorHelper("10+2*0/0"));
    }
    @Test
    void testCalculateV2MultipleExpression() {
        double delta = 0.0001; // for tolerance

        Assertions.assertEquals(3.00012207, calculatorHelper("3+4*2/(1-5)^2^3", false), delta);
    }

    @Test
    void testCalculateV2MultipleExpression2() {
        Assertions.assertEquals(-225, calculatorHelper("13^2-(9*11/0.25+(0.75))+(3.75-2^0)", false));
    }

    // decimal or floating values are harder to assess.
}
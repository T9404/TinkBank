package com.academy.fintech.unit;

import com.academy.fintech.core.service.approval.util.EstimateCalculator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EstimateCalculatorTest {

    @Test
    public void testSalaryScore() {
        int salary = 1000;
        int regularPayment = 300;
        int expectedScore = 1;
        int actualScore = EstimateCalculator.getSalaryScore(salary, regularPayment);
        Assertions.assertEquals(expectedScore, actualScore);
    }

    @Test
    public void testLargeLatePayment() {
        int expectedScore = -1;
        int actualScore = EstimateCalculator.calculateCreditBehaviorScore(true, false);
        Assertions.assertEquals(expectedScore, actualScore);
    }

    @Test
    public void testSmallLatePayment() {
        int expectedScore = 0;
        int actualScore = EstimateCalculator.calculateCreditBehaviorScore(false, true);
        Assertions.assertEquals(expectedScore, actualScore);
    }

    @Test
    public void testNoLatePayment() {
        int expectedScore = 1;
        int actualScore = EstimateCalculator.calculateCreditBehaviorScore(false, false);
        Assertions.assertEquals(expectedScore, actualScore);
    }
}

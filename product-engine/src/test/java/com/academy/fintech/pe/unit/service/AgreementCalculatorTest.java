package com.academy.fintech.pe.unit.service;

import com.academy.fintech.pe.core.service.agreement.util.AgreementCalculator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AgreementCalculatorTest {

    @Test
    public void testGetNextPaymentDateFirstMonthDay() {
        LocalDateTime disbursementDate = LocalDateTime.of(2023, 11, 1, 0, 0, 0);
        LocalDateTime expectedNextPaymentDate = LocalDateTime.of(2023, 12, 1, 0, 0, 0);
        LocalDateTime actualNextPaymentDate = AgreementCalculator.getNextPaymentDate(disbursementDate);

        assertEquals(expectedNextPaymentDate, actualNextPaymentDate);
    }

    @Test
    public void testGetNextPaymentDateLastMonthDay() {
        LocalDateTime disbursementDate = LocalDateTime.of(2023, 10, 31, 0, 0, 0);
        LocalDateTime expectedNextPaymentDate = LocalDateTime.of(2023, 11, 30, 0, 0, 0);
        LocalDateTime actualNextPaymentDate = AgreementCalculator.getNextPaymentDate(disbursementDate);

        assertEquals(expectedNextPaymentDate, actualNextPaymentDate);
    }

    @Test
    public void testCalculatePMT() {
        BigDecimal principal = new BigDecimal("500000");
        BigDecimal annualInterestRate = new BigDecimal("8");
        int numberOfMonths = 12;
        BigDecimal expectedPMT = new BigDecimal("43494.21454271053467522941546312793");
        BigDecimal actualPMT = AgreementCalculator.calculatePMT(principal, annualInterestRate, numberOfMonths);

        assertEquals(expectedPMT, actualPMT);
    }

    @Test
    public void testCalculateIPMTFirstPeriod() {
        BigDecimal principal = new BigDecimal("500000");
        BigDecimal annualInterestRate = new BigDecimal("10");
        int numberOfPeriods = 1;
        int period = 12;

        BigDecimal expectedIPMT = new BigDecimal("-43624.9368422956");
        BigDecimal actualIPMT = AgreementCalculator.calculateIPMT(principal, annualInterestRate, numberOfPeriods, period);
        expectedIPMT = expectedIPMT.setScale(10, RoundingMode.HALF_UP);
        actualIPMT = actualIPMT.setScale(10, RoundingMode.HALF_UP);

        assertEquals(expectedIPMT, actualIPMT);
    }

    @Test
    public void testCalculateIPMTLastPeriod() {
        BigDecimal principal = new BigDecimal("500000");
        BigDecimal annualInterestRate = new BigDecimal("10");
        int numberOfPeriods = 12;
        int period = 12;

        BigDecimal expectedIPMT = new BigDecimal("363.2887877995");
        BigDecimal actualIPMT = AgreementCalculator.calculateIPMT(principal, annualInterestRate, numberOfPeriods, period);
        expectedIPMT = expectedIPMT.setScale(10, RoundingMode.HALF_UP);
        actualIPMT = actualIPMT.setScale(10, RoundingMode.HALF_UP);

        assertEquals(expectedIPMT, actualIPMT);
    }

    @Test
    public void testCalculatePPMT() {
        BigDecimal principal = new BigDecimal("10000");
        BigDecimal annualInterestRate = new BigDecimal("5.0");
        int numberOfPeriods = 24;
        int period = 12;

        BigDecimal expectedPPMT = new BigDecimal("415.6291322947");
        BigDecimal actualPPMT = AgreementCalculator.calculatePPMT(principal, annualInterestRate, numberOfPeriods, period);
        expectedPPMT = expectedPPMT.setScale(2, RoundingMode.HALF_UP);
        actualPPMT = actualPPMT.setScale(2, RoundingMode.HALF_UP);

        assertEquals(expectedPPMT, actualPPMT);
    }

    @Test
    public void testCalculatePMT2() {
        BigDecimal principal = new BigDecimal("1005000");
        BigDecimal annualInterestRate = new BigDecimal("12");
        int numberOfMonths = 12;
        BigDecimal expectedPMT = new BigDecimal("89293.03262173341587668777038402600");
        BigDecimal actualPMT = AgreementCalculator.calculatePMT(principal, annualInterestRate, numberOfMonths);

        assertEquals(expectedPMT, actualPMT);
    }
}

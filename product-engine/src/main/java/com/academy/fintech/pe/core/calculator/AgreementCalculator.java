package com.academy.fintech.pe.core.calculator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;

public final class AgreementCalculator {

    /** Calculate next payment date
     * <br>
     * This method takes the provided payment date and calculates the timestamp for the next payment
     * by adding one month to the original payment date.
     */
    public static LocalDateTime getNextPaymentDate(LocalDateTime disbursementDate) {
        return disbursementDate.plusMonths(1);
    }

    /** Calculate PMT
     * <br>
     * More info in this
     * <a href="https://stackoverflow.com/questions/7827352/excel-pmt-function-in-java">page</a>
     * @param principal initial amount
     * @param annualInterestRate annual interest rate in percentage
     * @param numberOfMonths number of months
     * @return calculated PMT
     */
    public static BigDecimal calculatePMT(BigDecimal principal, BigDecimal annualInterestRate, int numberOfMonths) {
        BigDecimal monthlyInterestRate = annualInterestRate.divide(new BigDecimal("12"), MathContext.DECIMAL128)
                .divide(new BigDecimal("100"), MathContext.DECIMAL128);
        BigDecimal numerator = BigDecimal.ONE.add(monthlyInterestRate).pow(numberOfMonths, MathContext.DECIMAL128);
        BigDecimal denominator = BigDecimal.ONE.subtract(BigDecimal.ONE.
                divide(numerator, MathContext.DECIMAL128), MathContext.DECIMAL128);
        return principal.multiply(monthlyInterestRate, MathContext.DECIMAL128)
                .divide(denominator, MathContext.DECIMAL128);
    }

    /** Calculate IPMT
     * <br>
     * More info in this
     * <a href="https://answers.microsoft.com/en-us/msoffice/forum/all/what-is-the-equation-that-excel-uses-for-the-ipmt/2b2a7c0d-f39b-4fdc-a713-ba2810b3d166">page</a>
     * @param principal initial amount
     * @param annualInterestRate annual interest rate in percentage
     * @param numberOfPeriods number of months in the loan
     * @param period current period (month)
     * @return calculated IPMT
     */
    public static BigDecimal calculateIPMT(BigDecimal principal, BigDecimal annualInterestRate, int numberOfPeriods, int period) {
        return calculatePayment(principal, annualInterestRate, numberOfPeriods, period, true);
    }

    /** Calculate PPMT
     * <br>
     * More info in this
     * <a href="https://answers.microsoft.com/en-us/msoffice/forum/all/what-is-the-equation-that-excel-uses-for-the-ipmt/2b2a7c0d-f39b-4fdc-a713-ba2810b3d166">page</a>
     * @param principal initial amount
     * @param annualInterestRate annual interest rate in percentage
     * @param numberOfPeriods number of months in the loan
     * @param period current period (month)
     * @return calculated PPMT
     */
    public static BigDecimal calculatePPMT(BigDecimal principal, BigDecimal annualInterestRate, int numberOfPeriods, int period) {
        return calculatePayment(principal, annualInterestRate, numberOfPeriods, period, false);
    }

    /** Calculate next payment date
     * <br>
     * This method takes the provided payment date and calculates the timestamp for the specified
     * future period by adding the given number of months to the original payment date.
     */
    public static Timestamp calculateNextPaymentDate(Timestamp paymentDate, int period) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(paymentDate);
        calendar.add(Calendar.MONTH, period);
        return new Timestamp(calendar.getTimeInMillis());
    }

    private static BigDecimal calculatePayment(BigDecimal principal, BigDecimal annualInterestRate, int numberOfPeriods, int period, boolean isInterest) {
        BigDecimal monthlyInterestRate = annualInterestRate.divide(new BigDecimal("1200"), 10, RoundingMode.HALF_UP);
        BigDecimal outstandingBalance = principal;
        BigDecimal monthlyPayment = calculatePMT(outstandingBalance, annualInterestRate, numberOfPeriods);

        for (int i = 1; i < period; i++) {
            BigDecimal interestPayment = outstandingBalance.multiply(monthlyInterestRate);
            BigDecimal principalPayment = monthlyPayment.subtract(interestPayment);
            outstandingBalance = outstandingBalance.subtract(principalPayment);
        }

        return isInterest ? outstandingBalance.multiply(monthlyInterestRate) : monthlyPayment.subtract(outstandingBalance.multiply(monthlyInterestRate));
    }
}

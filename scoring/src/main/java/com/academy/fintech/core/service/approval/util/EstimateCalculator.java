package com.academy.fintech.core.service.approval.util;

public final class EstimateCalculator {

    public static int calculateCreditBehaviorScore(boolean hasLargeLatePayment, boolean hasSmallLatePayment) {
        if (hasLargeLatePayment) {
            return -1;
        } else if (hasSmallLatePayment) {
            return 0;
        }
        return 1;
    }

    public static int getSalaryScore(int salary, int regularPayment) {
        return (regularPayment > salary / 3) ? 0 : 1;
    }
}

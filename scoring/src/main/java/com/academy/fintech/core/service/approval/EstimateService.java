package com.academy.fintech.core.service.approval;

import com.academy.fintech.core.service.approval.product.engine.client.LatePaymentService;
import com.academy.fintech.core.service.approval.product.engine.client.PeriodPaymentService;
import com.example.payment.AcceptingScoringRequest;
import com.example.payment.LatePaymentRequest;
import com.example.payment.PeriodPaymentRequest;
import com.google.protobuf.Timestamp;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.academy.fintech.core.service.approval.mapper.PeriodPaymentRequestMapper.mapToPeriodPaymentRequest;
import static com.academy.fintech.core.service.approval.util.EstimateCalculator.calculateCreditBehaviorScore;
import static com.academy.fintech.core.service.approval.util.EstimateCalculator.getSalaryScore;
import static com.academy.fintech.core.service.approval.util.LatePaymentChecker.isLargeLatePayment;
import static com.academy.fintech.core.service.approval.util.LatePaymentChecker.isSmallLatePayment;

@Service
@RequiredArgsConstructor
public class EstimateService {
    private static final Logger logger =  LoggerFactory.getLogger(EstimateService.class);
    private final PeriodPaymentService periodPaymentService;
    private final LatePaymentService latePaymentService;

    @Value("${late.payment.threshold.days}")
    private int daysThreshold;

    public int estimate(AcceptingScoringRequest request) {
        logger.info("Estimating for user: {}", request.getUserId());

        int existingCreditsScore = checkExistingCredits(request);
        int regularPaymentScore = checkRegularPayment(request);
        int totalScore = existingCreditsScore + regularPaymentScore;

        logger.info("Estimation result for user {}: {}", request.getUserId(), totalScore);
        return totalScore;
    }

    private int checkExistingCredits(AcceptingScoringRequest request) {
        logger.debug("Checking existing credits for user: {}", request.getUserId());

        List<Timestamp> timestamps = latePaymentService.getLatePayment(LatePaymentRequest.newBuilder()
                .setUserId(request.getUserId())
                .build());

        boolean hasLargeLatePayment = timestamps.stream().anyMatch(timestamp -> isLargeLatePayment(timestamp, daysThreshold));
        boolean hasSmallLatePayment = timestamps.stream().anyMatch(timestamp -> isSmallLatePayment(timestamp, daysThreshold));
        int creditBehaviorScore = calculateCreditBehaviorScore(hasLargeLatePayment, hasSmallLatePayment);

        logger.debug("Credit behavior score for user {}: {}", request.getUserId(), creditBehaviorScore);
        return creditBehaviorScore;
    }

    private int checkRegularPayment(AcceptingScoringRequest request) {
        logger.debug("Checking regular payment for user: {}", request.getUserId());

        PeriodPaymentRequest periodPaymentRequest = mapToPeriodPaymentRequest(request);
        int regularPayment = periodPaymentService.getPeriodPayment(periodPaymentRequest);
        int salaryScore = getSalaryScore(request.getSalary(), regularPayment);

        logger.debug("Regular payment score for user {}: {}", request.getUserId(), salaryScore);
        return salaryScore;
    }
}

package com.academy.fintech.pe.core.service.balance;

import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementService;
import com.academy.fintech.pe.core.service.balance.db.Balance;
import com.academy.fintech.pe.core.service.balance.db.BalanceService;
import com.academy.fintech.pe.core.service.balance.db.enums.BalanceType;
import com.academy.fintech.pe.core.service.agreement.exception.AgreementNotFoundException;
import com.academy.fintech.pe.core.service.balance.exception.BalanceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class BalanceServiceV1 {
    private AgreementService agreementService;
    private BalanceService balanceService;

    @Transactional
    public void addPayment(String agreementId, BigDecimal amount) {
        var agreement = agreementService.findById(agreementId).orElseThrow(AgreementNotFoundException::new);

        createBalanceIfNotExists(agreement, BalanceType.DEBIT.name());
        createBalanceIfNotExists(agreement, BalanceType.CREDIT.name());

        var debitBalance = balanceService.findByAgreementIdAndType(agreementId, BalanceType.DEBIT.name())
                .orElseThrow(() -> new BalanceNotFoundException("Debit balance not found for agreementId: " + agreementId));

        debitBalance.setBalance(debitBalance.getBalance().add(amount));
        balanceService.save(debitBalance);
    }

    public void save(Balance balance) {
        balanceService.save(balance);
    }

    public Balance getBalance(String agreementId, BalanceType type) {
        return balanceService.findByAgreementIdAndType(agreementId, type.name())
                .orElseThrow(() -> new BalanceNotFoundException("Credit balance not found for agreementId: " + agreementId));
    }

    private void createBalanceIfNotExists(Agreement agreement, String type) {
        var balance = balanceService.findByAgreementIdAndType(agreement.getId(), type);

        if (balance.isEmpty()) {
            var newBalance = createBalanceEntity(agreement, type);
            balanceService.save(newBalance);
        }
    }

    private Balance createBalanceEntity(Agreement agreement, String type) {
        return Balance.builder()
                .agreement(agreement)
                .type(type)
                .balance(BigDecimal.ZERO)
                .build();
    }
}

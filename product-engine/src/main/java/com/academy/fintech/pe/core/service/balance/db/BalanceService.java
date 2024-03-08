package com.academy.fintech.pe.core.service.balance.db;

import com.academy.fintech.pe.core.service.balance.db.enums.BalanceType;
import com.academy.fintech.pe.core.service.balance.exception.BalanceTypeException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class BalanceService {
    private final BalanceRepository balanceRepository;

    public Optional<Balance> findByAgreementIdAndType(String agreementId, String type) {
        return balanceRepository.findByAgreementIdAndType(agreementId, type);
    }

    public void save(Balance balance) {
        checkBalanceType(balance);
        balanceRepository.save(balance);
    }

    private void checkBalanceType(Balance balance) {
        try {
            BalanceType.valueOf(balance.getType());
        } catch (IllegalArgumentException e) {
            throw new BalanceTypeException("Invalid balance type: " + balance.getType());
        }
    }
}

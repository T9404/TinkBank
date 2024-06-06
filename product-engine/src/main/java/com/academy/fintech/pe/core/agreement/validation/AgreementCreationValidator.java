package com.academy.fintech.pe.core.agreement.validation;

import com.academy.fintech.pe.core.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.agreement.db.product.Product;
import com.academy.fintech.pe.core.agreement.exception.AgreementValidationException;

public final class AgreementCreationValidator {

    public static void checkAgreementValidity(Agreement agreement, Product product) {
        checkPrincipalAmount(agreement, product);
        checkLoanTerm(agreement, product);
        checkOriginationAmount(agreement, product);
        checkInterestRate(agreement, product);
    }

    private static void checkPrincipalAmount(Agreement agreement, Product product) {
        if (agreement.getPrincipalAmount() == null) {
            throw new AgreementValidationException("Principal amount cannot be null for " + product.getCode() + " product");
        }
        if (agreement.getPrincipalAmount().compareTo(product.getMinPrincipalAmount()) < 0 ||
                agreement.getPrincipalAmount().compareTo(product.getMaxPrincipalAmount()) > 0) {
            throw new AgreementValidationException("Principal amount must be between " + product.getMinPrincipalAmount() +
                    " and " + product.getMaxPrincipalAmount() + " for " + product.getCode() + " product");
        }
    }

    private static void checkLoanTerm(Agreement agreement, Product product) {
        if (agreement.getTerm() < product.getMinTerm() || agreement.getTerm() > product.getMaxTerm()) {
            throw new AgreementValidationException("Term must be between " + product.getMinTerm() + " and " +
                    product.getMaxTerm() + " months for " + product.getCode() + " product");
        }
    }

    private static void checkOriginationAmount(Agreement agreement, Product product) {
        if (agreement.getOriginationAmount() == null) {
            throw new AgreementValidationException("Origination amount cannot be null for " + product.getCode() + " product");
        }
        if (agreement.getOriginationAmount().compareTo(product.getMinOriginationAmount()) < 0 ||
                agreement.getOriginationAmount().compareTo(product.getMaxOriginationAmount()) > 0) {
            throw new AgreementValidationException("Origination amount must be between " + product.getMinOriginationAmount() +
                    " and " + product.getMaxOriginationAmount() + " for " + product.getCode() + " product");
        }
    }

    private static void checkInterestRate(Agreement agreement, Product product) {
        if (agreement.getInterest() == null) {
            throw new AgreementValidationException("Interest rate cannot be null for " + product.getCode() + " product");
        }
        if (agreement.getInterest().compareTo(product.getMinInterest()) < 0 ||
                agreement.getInterest().compareTo(product.getMaxInterest()) > 0) {
            throw new AgreementValidationException("Interest rate must be between " + product.getMinInterest() +
                    " and " + product.getMaxInterest() + " for " + product.getCode() + " product");
        }
    }
}

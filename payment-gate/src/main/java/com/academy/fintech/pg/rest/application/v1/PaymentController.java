package com.academy.fintech.pg.rest.application.v1;

import com.academy.fintech.pg.public_interface.application.dto.DisbursementDto;
import com.academy.fintech.pg.grps.application.v1.DisbursementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("payment")
public class PaymentController {
    private final DisbursementService disbursementService;

    public PaymentController(DisbursementService disbursementService) {
        this.disbursementService = disbursementService;
    }

    @PostMapping("disburse")
    public ResponseEntity<String> disburse(@RequestBody DisbursementDto request) {
        return ResponseEntity.ok(disbursementService.disburse(request));
    }
}

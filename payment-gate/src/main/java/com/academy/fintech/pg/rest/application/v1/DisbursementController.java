package com.academy.fintech.pg.rest.application.v1;

import com.academy.fintech.pg.public_interface.application.v1.DisbursementDto;
import com.academy.fintech.pg.grps.application.v1.DisbursementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("disbursements")
public class DisbursementController {
    private final DisbursementService disbursementService;

    @PostMapping()
    public ResponseEntity<String> disburse(@RequestBody DisbursementDto request) {
        return ResponseEntity.ok(disbursementService.disburse(request));
    }
}

package tech.jes.jbank.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jes.jbank.controller.dto.TransferMoneyDto;
import tech.jes.jbank.service.TransferService;

@RestController
@RequestMapping(path = "/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<Void> transfer(@RequestBody @Valid TransferMoneyDto dto) {

        transferService.transferMoney(dto);

        return ResponseEntity.ok().build();
    }
}

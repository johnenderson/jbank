package tech.jes.jbank.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jes.jbank.controller.dto.TransferMoneyDto;
import tech.jes.jbank.entities.Transfer;
import tech.jes.jbank.entities.Wallet;
import tech.jes.jbank.exception.TransferException;
import tech.jes.jbank.exception.WalletNotFoundException;
import tech.jes.jbank.repository.TransferRepository;
import tech.jes.jbank.repository.WalletRepository;

import java.time.LocalDateTime;

@Service
public class TransferService {

    private final TransferRepository transferRepository;
    private final WalletRepository walletRepository;

    public TransferService(TransferRepository transferRepository,
                           WalletRepository walletRepository) {
        this.transferRepository = transferRepository;
        this.walletRepository = walletRepository;
    }

    @Transactional
    public void transferMoney(TransferMoneyDto dto) {

        var sender = walletRepository.findById(dto.sender())
                .orElseThrow(() -> new WalletNotFoundException("sender does not exist"));


        var receiver = walletRepository.findById(dto.receiver())
                .orElseThrow(() -> new WalletNotFoundException("receiver does not exist"));

        if (sender.getBalance().compareTo(dto.value()) == -1) {
            throw new TransferException("insufficient balance. you current balance is $" + sender.getBalance());
        }

        persistTransfer(dto, receiver, sender);
        updateWallets(dto, sender, receiver);
    }

    private void updateWallets(TransferMoneyDto dto, Wallet sender, Wallet receiver) {
        sender.setBalance(sender.getBalance().subtract(dto.value()));
        receiver.setBalance(receiver.getBalance().add(dto.value()));
        walletRepository.save(sender);
        walletRepository.save(receiver);
    }

    private void persistTransfer(TransferMoneyDto dto, Wallet receiver, Wallet sender) {
        var transfer = new Transfer();
        transfer.setReceiver(receiver);
        transfer.setSender(sender);
        transfer.setTransferValue(dto.value());
        transfer.setTransferDateTime(LocalDateTime.now());

        transferRepository.save(transfer);
    }
}

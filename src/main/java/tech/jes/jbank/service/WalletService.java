package tech.jes.jbank.service;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jes.jbank.controller.dto.CreateWalletDto;
import tech.jes.jbank.controller.dto.DepositMoneyDto;
import tech.jes.jbank.entities.Deposit;
import tech.jes.jbank.entities.Wallet;
import tech.jes.jbank.exception.DeleteWalletException;
import tech.jes.jbank.exception.WalletDataAlreadyExistsException;
import tech.jes.jbank.exception.WalletNotFoundException;
import tech.jes.jbank.repository.DepositRepository;
import tech.jes.jbank.repository.WalletRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final DepositRepository depositRepository;

    public WalletService(WalletRepository walletRepository,
                         DepositRepository depositRepository) {
        this.walletRepository = walletRepository;
        this.depositRepository = depositRepository;
    }

    public Wallet createWallet(CreateWalletDto dto) {

        var walletDb = walletRepository.findByCpfOrEmail(dto.cpf(), dto.email());

        if (walletDb.isPresent()) {
            throw new WalletDataAlreadyExistsException("cpf or email already exists");

        }

        var wallet = new Wallet();
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setName(dto.name());
        wallet.setCpf(dto.cpf());
        wallet.setEmail(dto.email());

        return walletRepository.save(wallet);
    }

    public boolean deleteWallet(UUID walletId) {

        var wallet = walletRepository.findById(walletId);

        if (wallet.isPresent()) {

            if (wallet.get().getBalance().compareTo(BigDecimal.ZERO) != 0) {
                throw new DeleteWalletException(
                        "The balance is not zero. The current amount is $" + wallet.get().getBalance());
            }
            walletRepository.deleteById(walletId);

        }

        return wallet.isPresent();
    }

    @Transactional
    public void depositMoney(UUID walletId, @Valid DepositMoneyDto dto, String ipAddress) {

        var wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("there is no wallet with this id"));


        var deposit = new Deposit();
        deposit.setWallet(wallet);
        deposit.setDepositValue(dto.value());
        deposit.setDepositDatetime(LocalDateTime.now());
        deposit.setIpAddress(ipAddress);

        depositRepository.save(deposit);

        wallet.setBalance(wallet.getBalance().add(dto.value()));

        walletRepository.save(wallet);
    }
}

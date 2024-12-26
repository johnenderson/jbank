package tech.jes.jbank.service;

import org.springframework.stereotype.Service;
import tech.jes.jbank.controller.dto.CreateWalletDto;
import tech.jes.jbank.entities.Wallet;
import tech.jes.jbank.exception.DeleteWalletException;
import tech.jes.jbank.exception.WalletDataAlreadyExistsException;
import tech.jes.jbank.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
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
}

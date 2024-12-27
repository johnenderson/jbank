package tech.jes.jbank.service;

import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jes.jbank.controller.dto.CreateWalletDto;
import tech.jes.jbank.controller.dto.DepositMoneyDto;
import tech.jes.jbank.controller.dto.PaginationDto;
import tech.jes.jbank.controller.dto.StatementDto;
import tech.jes.jbank.controller.dto.StatementItemDto;
import tech.jes.jbank.controller.dto.StatementOperation;
import tech.jes.jbank.controller.dto.WalletDto;
import tech.jes.jbank.entities.Deposit;
import tech.jes.jbank.entities.Wallet;
import tech.jes.jbank.exception.DeleteWalletException;
import tech.jes.jbank.exception.StatementException;
import tech.jes.jbank.exception.WalletDataAlreadyExistsException;
import tech.jes.jbank.exception.WalletNotFoundException;
import tech.jes.jbank.repository.DepositRepository;
import tech.jes.jbank.repository.WalletRepository;
import tech.jes.jbank.repository.dto.StatementView;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    public StatementDto getStatements(UUID walletId, Integer page, Integer pageSize) {

        var wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("there is no wallet with this id"));

        var pageRequest = PageRequest.of(page, pageSize, Sort.Direction.DESC, "statement_date_time");

        var statements = walletRepository.findStatements(walletId.toString(), pageRequest)
                .map(view -> mapToDto(walletId, view));

        return new StatementDto(
                new WalletDto(
                        wallet.getWalletId(),
                        wallet.getCpf(),
                        wallet.getName(),
                        wallet.getEmail(),
                        wallet.getBalance()
                ),
                statements.getContent(),
                new PaginationDto(
                        statements.getNumber(),
                        statements.getSize(),
                        statements.getTotalElements(),
                        statements.getTotalPages()
                )
        );
    }

    private StatementItemDto mapToDto(UUID walletId, StatementView view) {

        if (view.getType().equalsIgnoreCase("deposit")) {
            return mapToDeposit(view);
        }

        if (view.getType().equalsIgnoreCase("transfer")
                && view.getWalletSender().equalsIgnoreCase(walletId.toString())) {
            return mapWhenTransferSent(walletId, view);
        }

        if (view.getType().equalsIgnoreCase("transfer")
                && view.getWalletReceiver().equalsIgnoreCase(walletId.toString())) {
            return mapWhenTransferReceived(walletId, view);
        }

        throw new StatementException("invalid type " + view.getType());
    }

    private StatementItemDto mapWhenTransferReceived(UUID walletId, StatementView view) {
        return new StatementItemDto(
                view.getStatementId(),
                view.getType(),
                "money received from " + view.getWalletSender(),
                view.getStatementValue(),
                view.getStatementDateTime(),
                StatementOperation.CREDIT
        );
    }

    private StatementItemDto mapWhenTransferSent(UUID walletId, StatementView view) {
        return new StatementItemDto(
                view.getStatementId(),
                view.getType(),
                "money sent to " + view.getWalletReceiver(),
                view.getStatementValue(),
                view.getStatementDateTime(),
                StatementOperation.DEBIT
        );
    }

    private static StatementItemDto mapToDeposit(StatementView view) {
        return new StatementItemDto(
                view.getStatementId(),
                view.getType(),
                "money deposit",
                view.getStatementValue(),
                view.getStatementDateTime(),
                StatementOperation.CREDIT
        );
    }
}

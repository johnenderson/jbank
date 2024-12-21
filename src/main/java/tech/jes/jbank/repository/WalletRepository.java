package tech.jes.jbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.jes.jbank.entities.Wallet;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    Optional<Wallet> findByCpfOrEmail(String cpf, String email);
}

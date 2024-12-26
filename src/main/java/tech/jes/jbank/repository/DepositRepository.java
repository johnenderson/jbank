package tech.jes.jbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.jes.jbank.entities.Deposit;

import java.util.UUID;

public interface DepositRepository extends JpaRepository<Deposit, UUID> {
}

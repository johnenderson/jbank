package tech.jes.jbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.jes.jbank.entities.Transfer;

import java.util.UUID;

public interface TransferRepository extends JpaRepository<Transfer, UUID> {
}

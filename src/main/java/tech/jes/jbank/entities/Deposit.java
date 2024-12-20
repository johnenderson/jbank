package tech.jes.jbank.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_deposits")
public class Deposit {

    @Id
    @Column(name = "deposit_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID depositID;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @Column(name = "deposit_value")
    private BigDecimal depositValue;

    @Column(name = "deposit_date_time")
    private LocalDateTime depositDatetime;

    @Column(name = "ip_address")
    private String ipAddress;

    public Deposit() {
    }

    public UUID getDepositID() {
        return depositID;
    }

    public void setDepositID(UUID depositID) {
        this.depositID = depositID;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public BigDecimal getDepositValue() {
        return depositValue;
    }

    public void setDepositValue(BigDecimal depositValue) {
        this.depositValue = depositValue;
    }

    public LocalDateTime getDepositDatetime() {
        return depositDatetime;
    }

    public void setDepositDatetime(LocalDateTime depositDatetime) {
        this.depositDatetime = depositDatetime;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}

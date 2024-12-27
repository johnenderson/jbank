package tech.jes.jbank.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record StatementItemDto(String statementId,
                               String type,
                               String literal,
                               BigDecimal value,
                               LocalDateTime dateTime,
                               StatementOperation operation) {
}

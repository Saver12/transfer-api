package com.saver12.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public final class TransferDTO {
    private final Long sourceAccountId;
    private final Long destinationAccountId;
    private final String currency;
    private final BigDecimal amount;
}

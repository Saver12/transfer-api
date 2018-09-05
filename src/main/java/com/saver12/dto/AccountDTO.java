package com.saver12.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public final class AccountDTO {
    private final Long userId;
    private final BigDecimal balance;
    private final String currency;
}

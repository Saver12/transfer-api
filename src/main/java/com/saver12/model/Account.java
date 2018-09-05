package com.saver12.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Currency;

@Data
public final class Account {
    private final long id;
    private final long userId;
    private final BigDecimal balance;
    private final Currency currency;
}

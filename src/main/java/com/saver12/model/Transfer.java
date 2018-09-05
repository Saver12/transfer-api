package com.saver12.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Currency;

@Data
public class Transfer {
    private final long id;
    private final long sourceAccountId;
    private final long destinationAccountId;
    private final Currency currency;
    private final BigDecimal amount;
    private final Status status;
    private final String message;

    public enum Status {
        EXECUTED,
        FAILED
    }
}

package com.damonyuan.matchingengine.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Trade {
    private final long sequenceId;
    private final String symbol;
    private final Side side;
    private final String party; // taker
    private final String counterparty; // maker
    private final BigDecimal price;
    private final BigDecimal quantity;
}

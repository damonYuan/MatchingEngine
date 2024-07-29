package com.damonyuan.matchingengine.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Order {
    private final long sequenceId;
    private final String symbol;
    private final String party;
    private final Side side;
    private final BigDecimal price;
    private BigDecimal quantity;
    private OrderStatus status;

    public OrderKey getOrderKey() {
        return new OrderKey(price, sequenceId);
    }

    public void update(final BigDecimal quantity, final OrderStatus status) {
        this.quantity = quantity;
        this.status = status;
    }
}

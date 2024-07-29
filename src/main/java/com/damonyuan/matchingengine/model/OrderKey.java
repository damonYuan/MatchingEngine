package com.damonyuan.matchingengine.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderKey {
    private final BigDecimal price;
    private final long sequenceId;
}

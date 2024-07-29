package com.damonyuan.matchingengine.model;

public interface OrderBook {
    Order getFirst();

    boolean add(final Order order);

    boolean update(final Order order);

    boolean remove(final Order order);
}

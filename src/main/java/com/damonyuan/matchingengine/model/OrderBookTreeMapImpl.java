package com.damonyuan.matchingengine.model;

import lombok.Getter;

import java.util.TreeMap;

import static com.damonyuan.matchingengine.model.Side.BUY;
import static com.damonyuan.matchingengine.util.ComparatorUtils.getOrderKeyComparator;

public class OrderBookTreeMapImpl implements OrderBook {

    @Getter
    private final Side side;
    private final TreeMap<OrderKey, Order> map;

    public OrderBookTreeMapImpl(final Side side) {
        this.side = side;
        this.map = new TreeMap<>(side == BUY ? getOrderKeyComparator() : getOrderKeyComparator().reversed());
    }

    @Override
    public Order getFirst() {
        return map.isEmpty() ? null : map.firstEntry().getValue();
    }

    @Override
    public boolean add(final Order order) {
        final OrderKey key = order.getOrderKey();
        if (!map.containsKey(key)) {
            return map.put(key, order) == null;
        }
        return false;
    }

    @Override
    public boolean update(final Order order) {
        final OrderKey key = order.getOrderKey();
        if (map.containsKey(key)) {
            map.put(key, order);
            return true;
        }
        return false;
    }


    @Override
    public boolean remove(final Order order) {
        final OrderKey key = order.getOrderKey();
        if (map.containsKey(key)) {
            map.remove(key);
            return true;
        }
        return false;
    }
}

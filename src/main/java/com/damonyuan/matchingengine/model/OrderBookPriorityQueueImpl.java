package com.damonyuan.matchingengine.model;

import lombok.Getter;

import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import static com.damonyuan.matchingengine.model.Side.BUY;
import static com.damonyuan.matchingengine.util.ComparatorUtils.getOrderComparator;

public class OrderBookPriorityQueueImpl implements OrderBook {
    @Getter
    private final Side side;
    private final PriorityQueue<Order> queue;

    public OrderBookPriorityQueueImpl(final Side side) {
        this.side = side;
        this.queue = new PriorityQueue<>(side == BUY ? getOrderComparator() : getOrderComparator().reversed());
    }

    @Override
    public Order getFirst() {
        return queue.peek();
    }

    @Override
    public boolean add(final Order order) {
        return queue.offer(order);
    }

    @Override
    public boolean update(final Order order) {
        final List<Order> orders = queue.stream()
                .filter(o -> o.getPrice().compareTo(order.getPrice()) == 0 &&
                        o.getSequenceId() == order.getSequenceId())
                .collect(Collectors.toList());
        if (orders.size() > 1) {
            throw new RuntimeException("Unknown Error: matching orders size should <= 1");
        } else if (orders.size() == 1) {
            remove(orders.get(0));
            return add(order);
        } else {
            return false;
        }
    }

    @Override
    public boolean remove(final Order order) {
        final List<Order> orders = queue.stream()
                .filter(o -> o.getPrice().compareTo(order.getPrice()) == 0 &&
                        o.getSequenceId() == order.getSequenceId())
                .collect(Collectors.toList());
        if (orders.size() > 1) {
            throw new RuntimeException("Unknown Error: matching orders size should <= 1");
        } else if (orders.size() == 1) {
            return queue.remove(orders.get(0));
        } else {
            return false;
        }
    }
}

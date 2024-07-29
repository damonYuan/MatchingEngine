package com.damonyuan.matchingengine.service;

import com.damonyuan.matchingengine.model.Order;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

public class OrderProviderImpl implements OrderProvider {
    private final PriorityBlockingQueue<Order> q = new PriorityBlockingQueue<>(10,
            Comparator.comparingLong(Order::getSequenceId));

    @Override
    public void addOrder(final Order order) {
        q.offer(order);
    }


    @Override
    public Order getOrder() {
        try {
            return q.take();
        } catch (final InterruptedException ex) {
            Thread.currentThread().interrupt(); // when it's not easy to change the interface signature
            return null;
        }
    }
}

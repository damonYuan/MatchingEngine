package com.damonyuan.matchingengine.service;

import com.damonyuan.matchingengine.model.Order;

public interface OrderProvider {
    void addOrder(final Order order);

    Order getOrder();
}

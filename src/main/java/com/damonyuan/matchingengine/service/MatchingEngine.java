package com.damonyuan.matchingengine.service;

import com.damonyuan.matchingengine.model.Order;
import com.damonyuan.matchingengine.model.Trade;

public interface MatchingEngine extends Runnable {
    void processOrder(final Order order);

    void processTrade(final Trade trade);
}

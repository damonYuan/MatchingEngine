package com.damonyuan.matchingengine.service;

import com.damonyuan.matchingengine.model.Trade;

public class TradeProcessorImpl implements TradeProcessor {
    @Override
    public void processTrade(final Trade trade) {
        System.out.println(trade);
    }
}

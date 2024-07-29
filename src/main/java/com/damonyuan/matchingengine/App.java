package com.damonyuan.matchingengine;

import com.damonyuan.matchingengine.model.OrderBook;
import com.damonyuan.matchingengine.model.OrderBookTreeMapImpl;
import com.damonyuan.matchingengine.service.MatchingEngine;
import com.damonyuan.matchingengine.service.MatchingEngineImpl;
import com.damonyuan.matchingengine.service.OrderProvider;
import com.damonyuan.matchingengine.service.OrderProviderImpl;
import com.damonyuan.matchingengine.service.TradeProcessor;
import com.damonyuan.matchingengine.service.TradeProcessorImpl;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.damonyuan.matchingengine.model.Side.BUY;
import static com.damonyuan.matchingengine.model.Side.SELL;


public class App {
    public static void main(final String[] args) {
        final OrderProvider orderProvider = new OrderProviderImpl();
        final TradeProcessor tradeProcessor = new TradeProcessorImpl();
        final OrderBook buyOrderBook = new OrderBookTreeMapImpl(BUY);
        final OrderBook sellOrderBook = new OrderBookTreeMapImpl(SELL);
        final String symbol = "symbol";
        final MatchingEngine matchingEngine = new MatchingEngineImpl(symbol,
                orderProvider,
                tradeProcessor,
                buyOrderBook,
                sellOrderBook);
        final Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(matchingEngine);
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            System.out.printf("thread ID: %s; thread name: %s", t.getId(), t.getName());
            e.printStackTrace();
        });
    }
}

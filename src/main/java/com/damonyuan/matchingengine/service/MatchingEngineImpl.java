package com.damonyuan.matchingengine.service;

import com.damonyuan.matchingengine.model.Order;
import com.damonyuan.matchingengine.model.OrderBook;
import com.damonyuan.matchingengine.model.Trade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.damonyuan.matchingengine.model.OrderStatus.FULLY_FILLED;
import static com.damonyuan.matchingengine.model.OrderStatus.PARTIALLY_FILLED;
import static com.damonyuan.matchingengine.model.Side.BUY;
import static com.damonyuan.matchingengine.model.Side.SELL;

public class MatchingEngineImpl implements MatchingEngine {
    private final String symbol;
    private final OrderProvider provider;
    private final TradeProcessor producer;
    private final OrderBook buyBook;
    private final OrderBook sellBook;

    public MatchingEngineImpl(
            final String symbol,
            final OrderProvider provider,
            final TradeProcessor producer,
            final OrderBook buyBook,
            final OrderBook sellBook) {
        this.symbol = symbol;
        this.provider = provider;
        this.producer = producer;
        this.buyBook = buyBook;
        this.sellBook = sellBook;
    }

    private static boolean noPriceCross(final Order makerOrder, final Order takerOrder) {
        return (takerOrder.getSide() == BUY && takerOrder.getPrice().compareTo(makerOrder.getPrice()) < 0)
                || (takerOrder.getSide() == SELL && takerOrder.getPrice().compareTo(makerOrder.getPrice()) > 0);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) { // make it more responsive to the interruption
            final Order order = provider.getOrder();
            if (order != null) {
                processOrder(order);
            }
            Thread.yield(); // give some opportunity to other tasks
        }
    }

    @Override
    public void processOrder(final Order order) {
        final List<Trade> trades;
        if (order.getSide() == BUY) {
            trades = processOrder(order, sellBook, buyBook);
        } else {
            trades = processOrder(order, buyBook, sellBook);
        }
        trades.forEach(this::processTrade);
    }

    private List<Trade> processOrder(final Order takerOrder,
                                     final OrderBook makerBook,
                                     final OrderBook anotherBook) {
        final List<Trade> trades = new ArrayList<>();
        while (true) {
            final Order makerOrder = makerBook.getFirst();
            if (makerOrder == null || noPriceCross(makerOrder, takerOrder)) {
                anotherBook.add(takerOrder);
                break;
            }

            matchingOrders(trades, takerOrder, makerOrder);

            if (makerOrder.getQuantity().signum() == 0) {
                makerBook.remove(makerOrder);
            }
            if (takerOrder.getQuantity().signum() == 0) {
                break;
            }
        }
        return trades;
    }

    private void matchingOrders(final List<Trade> trades,
                                final Order takerOrder,
                                final Order makerOrder) {
        final BigDecimal matchingQty = takerOrder.getQuantity().min(makerOrder.getQuantity());
        final BigDecimal matchingPx = makerOrder.getPrice();
        if (takerOrder.getQuantity().compareTo(matchingQty) > 0) {
            // taker order partially filled
            takerOrder.update(takerOrder.getQuantity().subtract(matchingQty), PARTIALLY_FILLED);
            // maker order fully filled
            makerOrder.update(BigDecimal.ZERO, FULLY_FILLED);
            // generate trade
            trades.add(new Trade(System.currentTimeMillis(),
                    symbol,
                    takerOrder.getSide(),
                    takerOrder.getParty(),
                    makerOrder.getParty(),
                    matchingPx,
                    matchingQty));
        } else {
            // taker order fully filled
            takerOrder.update(BigDecimal.ZERO, FULLY_FILLED);
            // maker order partially filled
            makerOrder.update(makerOrder.getQuantity().subtract(matchingQty), PARTIALLY_FILLED);
            // generate trade
            trades.add(new Trade(System.currentTimeMillis(),
                    symbol,
                    takerOrder.getSide(),
                    takerOrder.getParty(),
                    makerOrder.getParty(),
                    matchingPx,
                    matchingQty));
        }
    }

    @Override
    public void processTrade(final Trade trade) {
        producer.processTrade(trade);
    }
}

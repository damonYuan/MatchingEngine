package com.damonyuan.matchingengine;

import com.damonyuan.matchingengine.model.*;
import com.damonyuan.matchingengine.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.damonyuan.matchingengine.model.OrderStatus.PENDING;
import static com.damonyuan.matchingengine.model.Side.BUY;
import static com.damonyuan.matchingengine.model.Side.SELL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Unit test for simple App.
 */
class AppTest {
    private static final String symbol = "symbol";
    private List<Order> orders;
    private List<Trade> expectedTrades;

    @BeforeEach
    public void beforeEach() {
        final Order order1 = new Order(1, symbol, "A", BUY, BigDecimal.valueOf(4.9), BigDecimal.valueOf(100), PENDING);
        final Order order2 = new Order(2, symbol, "B", BUY, BigDecimal.valueOf(5), BigDecimal.valueOf(20), PENDING);
        final Order order3 = new Order(3, symbol, "C", SELL, BigDecimal.valueOf(3.5), BigDecimal.valueOf(10), PENDING);
        final Order order4 = new Order(4, symbol, "D", SELL, BigDecimal.valueOf(4.0), BigDecimal.valueOf(50), PENDING);
        orders = List.of(order1, order2, order3, order4);
        final Trade expectedTrade1 = new Trade(1,
                symbol,
                SELL,
                "C",
                "B",
                BigDecimal.valueOf(5),
                BigDecimal.valueOf(10));
        final Trade expectedTrade2 = new Trade(2,
                symbol,
                SELL,
                "D",
                "B",
                BigDecimal.valueOf(5),
                BigDecimal.valueOf(10));
        final Trade expectedTrade3 = new Trade(3,
                symbol,
                SELL,
                "D",
                "A",
                BigDecimal.valueOf(4.9),
                BigDecimal.valueOf(40));
        expectedTrades = List.of(expectedTrade1, expectedTrade2, expectedTrade3);
    }

    @Test
    void givenOrdersAndTreeMapBasedOrderBook_whenOrdersReceived_thenItShouldProduceValidTrades()
            throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(3);

        final List<Trade> trades = new ArrayList<>();
        final OrderProvider orderProvider = new OrderProviderImpl();
        final TradeProcessor tradeProcessor = trade -> {
            System.out.println(trade);
            trades.add(trade);
            latch.countDown();
        };
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
        orders.forEach(orderProvider::addOrder);
        final boolean result = latch.await(1, TimeUnit.SECONDS);

        assertTrue(result);
        assertThat(trades).hasSize(3);
        assertThat(trades).usingRecursiveComparison()
                .ignoringFields("sequenceId")
                .isEqualTo(expectedTrades);
    }

    /**
     * Based on simple benchmark the performance is worse compared to TreeMap based implementation,
     * and O(logn) time complexity in Priority Queue's remove may be the convict.
     *
     * @throws InterruptedException
     */
    @Test
    void givenOrdersAndPriorityQueueBasedOrderBook_whenOrdersReceived_thenItShouldProduceValidTrades()
            throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(3);

        final List<Trade> trades = new ArrayList<>();
        final OrderProvider orderProvider = new OrderProviderImpl();
        final TradeProcessor tradeProcessor = trade -> {
            trades.add(trade);
            latch.countDown();
        };
        final OrderBook buyOrderBook = new OrderBookPriorityQueueImpl(BUY);
        final OrderBook sellOrderBook = new OrderBookPriorityQueueImpl(SELL);
        final String symbol = "symbol";
        final MatchingEngine matchingEngine = new MatchingEngineImpl(symbol,
                orderProvider,
                tradeProcessor,
                buyOrderBook,
                sellOrderBook);
        final Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(matchingEngine);
        orders.forEach(orderProvider::addOrder);
        final boolean result = latch.await(1, TimeUnit.SECONDS);

        assertTrue(result);
        assertThat(trades).hasSize(3);
        assertThat(trades).usingRecursiveComparison()
                .ignoringFields("sequenceId")
                .isEqualTo(expectedTrades);
    }
}

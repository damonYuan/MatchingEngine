package com.damonyuan.matchingengine.util;

import com.damonyuan.matchingengine.model.Order;
import com.damonyuan.matchingengine.model.OrderKey;

import java.util.Comparator;

import static java.lang.Long.compare;

public class ComparatorUtils {
    private static final Comparator<OrderKey> ORDER_KEY_COMPARATOR = (o1, o2) -> {
        if (o1.getPrice().compareTo(o2.getPrice()) > 0) {
            return -1;
        } else if (o1.getPrice().compareTo(o2.getPrice()) < 0) {
            return 1;
        } else {
            return compare(o1.getSequenceId(), o2.getSequenceId());
        }
    };
    private static final Comparator<Order> ORDER_COMPARATOR = (o1, o2) -> {
        if (o1.getPrice().compareTo(o2.getPrice()) > 0) {
            return -1;
        } else if (o1.getPrice().compareTo(o2.getPrice()) < 0) {
            return 1;
        } else {
            return compare(o1.getSequenceId(), o2.getSequenceId());
        }
    };

    private ComparatorUtils() {
    }

    public static Comparator<OrderKey> getOrderKeyComparator() {
        return ORDER_KEY_COMPARATOR;
    }


    public static Comparator<Order> getOrderComparator() {
        return ORDER_COMPARATOR;
    }
}

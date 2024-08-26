package com.damonyuan.matchingengine.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

public class RoomManagerImpl implements RoomManager {
    @Override
    public int getRooms(int[] checkIn, int[] checkOut) {
        if (checkIn == null || checkOut == null) {
            throw new RuntimeException("invalid input");
        }
        if (checkIn.length != checkOut.length) {
            throw new RuntimeException("invalid length");
        }
        if (checkIn.length == 0) {
            return 0;
        }

        final List<Integer> absSortedInAndOut = getAbsSortedCombinedList(checkIn, checkOut);
        return solution2(absSortedInAndOut);
    }

    private static int solution2(List<Integer> absSortedInAndOut) {
        int res = 0;
        final Stack<Integer> s = new Stack<>();
        for (int e : absSortedInAndOut) {
            if (e > 0) {
                s.push(e);
                res = Math.max(res, s.size());
            } else {
                s.pop();
            }
        }
        return res;
    }

    private static int solution1(final List<Integer> absSortedInAndOut) {
        int res = 0;
        int rooms = 0;
        for (int e : absSortedInAndOut) {
            if (e > 0) {
                rooms++;
                res = Math.max(rooms, res);
            } else {
                rooms--;
            }
        }
        return res;
    }

    private static List<Integer> getAbsSortedCombinedList(int[] checkIn, int[] checkOut) {
        final List<Integer> inAndOut = new ArrayList<>();
        for (int i : checkIn) {
            inAndOut.add(i);
        }
        for (int o : checkOut) {
            inAndOut.add(-o);
        }
        inAndOut.sort(Comparator.comparingInt(Math::abs));
        return inAndOut;
    }
}

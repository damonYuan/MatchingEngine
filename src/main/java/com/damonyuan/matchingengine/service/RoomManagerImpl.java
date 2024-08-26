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

        return solution3(checkIn, checkOut);
    }

    // using ranges and brute-force
    private static int solution3(int[] checkIn, int[] checkOut) {
        final List<int[]> ranges = new ArrayList<>();
        for (int i = 0; i < checkIn.length; i++) {
            ranges.add(new int[] {checkIn[i], checkOut[i]});
        }
        ranges.sort(Comparator.comparingInt(a -> a[0]));
        int start = ranges.get(0)[0];
        int end = 0;
        for (int[] range : ranges) {
            end = Math.max(range[1], end);
        }

        int res = 0;
        for (int i = start; i <= end; i++) {
            int count = 0;
            for (int[] range : ranges) {
                if (i >= range[0] && i <= range[1]) {
                    count++;
                }
            }
            res = Math.max(res, count);
        }
        return res;
    }

    // using a stack
    private static int solution2(int[] checkIn, int[] checkOut) {
        final List<Integer> absSortedInAndOut = getAbsSortedCombinedList(checkIn, checkOut);
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

    // using a variable
    private static int solution1(int[] checkIn, int[] checkOut) {
        final List<Integer> absSortedInAndOut = getAbsSortedCombinedList(checkIn, checkOut);
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

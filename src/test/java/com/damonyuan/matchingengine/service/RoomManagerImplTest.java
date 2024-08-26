package com.damonyuan.matchingengine.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoomManagerImplTest {
    RoomManager roomManager = new RoomManagerImpl();

    @Test
    void givenNull_whenGetRooms_thenItShouldThrowException() {
        final RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            roomManager.getRooms(null, null);
        });
        assertEquals("invalid input", exception.getMessage());
    }

    @Test
    void givenInvalidLength_whenGetRooms_thenItShouldThrowException() {
        final RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            roomManager.getRooms(new int[]{1}, new int[]{2, 3});
        });
        assertEquals("invalid length", exception.getMessage());
    }

    @Test
    void givenEmptyInput_whenGetRooms_thenItShouldReturn0() {
        final int res = roomManager.getRooms(new int[]{}, new int[]{});
        assertEquals(0, res);
    }

    @Test
    void givenLength1_whenGetRooms_thenItShouldReturn1() {
        final int res = roomManager.getRooms(new int[]{1}, new int[]{2});
        assertEquals(1, res);
    }

    // [1, 2]
    //       [3, 4]
    //             [5, 6]
    @Test
    void givenLength3WithoutOverlapping_whenGetRooms_thenItShouldReturn1() {
        final int res = roomManager.getRooms(new int[]{1, 3, 5}, new int[]{2, 4, 6});
        assertEquals(1, res);
    }

    // [1, 3]
    //    [2, 4]
    //       [4, 5]
    @Test
    void givenLength3WithOverlapping1_whenGetRooms_thenItShouldReturn2() {
        final int res = roomManager.getRooms(new int[]{1, 2, 4}, new int[]{3, 4, 5});
        assertEquals(2, res);
    }

    // [1,              9]
    //   [2, 3]
    //          [5, 6]
    @Test
    void givenLength3WithOverlapping2_whenGetRooms_thenItShouldReturn2() {
        final int res = roomManager.getRooms(new int[]{1, 2, 5}, new int[]{9, 3, 6});
        assertEquals(2, res);
    }

    // [1,         6]
    //   [2, 4]
    //       [4, 5]
    @Test
    void givenLength3WithOverlapping3_whenGetRooms_thenItShouldReturn3() {
        final int res = roomManager.getRooms(new int[]{1, 2, 4}, new int[]{6, 4, 5});
        assertEquals(3, res);
    }
}
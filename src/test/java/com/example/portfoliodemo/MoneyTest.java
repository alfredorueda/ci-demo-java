package com.example.portfoliodemo;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoneyTest {

    @Test
    void roundsToTwoDecimalsHalfUp() {
        assertEquals(new BigDecimal("10.01"), Money.of("10.005").amount());
    }

    @Test
    void add() {
        assertEquals(Money.of("12.50"), Money.of(10).add(Money.of("2.50")));
    }

    @Test
    void subtract() {
        assertEquals(Money.of("7.50"), Money.of(10).subtract(Money.of("2.50")));
    }

    @Test
    void multiplyByQuantity() {
        assertEquals(Money.of(10), Money.of("2.50").multiply(4));
    }

    @Test
    void equalityIsByValueNotIdentity() {
        assertEquals(Money.of("5.00"), Money.of(5));
        assertNotSame(Money.of("5.00"), Money.of(5));
    }

    @Test
    void isNegative() {
        assertTrue(Money.of(-5).isNegative());
        assertFalse(Money.of(5).isNegative());
    }
}

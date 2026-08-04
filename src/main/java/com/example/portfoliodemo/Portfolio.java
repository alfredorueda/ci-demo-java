package com.example.portfoliodemo;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * Tracks cash and stock holdings for one investor.
 *
 * <p>Shares are sold FIFO (first lot bought is the first lot sold), which
 * is the standard convention for computing realized gains.</p>
 */
public class Portfolio {

    /** A batch of shares bought together at the same price. */
    private static final class Lot {
        private int quantity;
        private final Money price;

        private Lot(int quantity, Money price) {
            this.quantity = quantity;
            this.price = price;
        }
    }

    private Money cash;
    private final Map<String, Deque<Lot>> lots = new HashMap<>();

    public Portfolio() {
        this(Money.of(0));
    }

    public Portfolio(Money initialCash) {
        this.cash = initialCash;
    }

    public Money cashBalance() {
        return cash;
    }

    public int sharesOwned(String ticker) {
        return lots.getOrDefault(ticker, new ArrayDeque<>())
                .stream()
                .mapToInt(lot -> lot.quantity)
                .sum();
    }

    public void deposit(Money amount) {
        cash = cash.add(amount);
    }

    public void buy(String ticker, int quantity, Money price) {
        Money cost = price.multiply(quantity);
        if (cost.amount().compareTo(cash.amount()) > 0) {
            throw new InsufficientFundsException(
                    "Cannot buy %d %s: cost %s exceeds cash balance %s"
                            .formatted(quantity, ticker, cost, cash));
        }
        cash = cash.subtract(cost);
        lots.computeIfAbsent(ticker, t -> new ArrayDeque<>()).addLast(new Lot(quantity, price));
    }

    /** Sells shares FIFO and returns the realized gain (or loss, if negative). */
    public Money sell(String ticker, int quantity, Money price) {
        int available = sharesOwned(ticker);
        if (quantity > available) {
            throw new InsufficientSharesException(
                    "Cannot sell %d %s: only %d shares owned".formatted(quantity, ticker, available));
        }

        Deque<Lot> tickerLots = lots.get(ticker);
        int remainingToSell = quantity;
        Money costBasis = Money.of(0);

        while (remainingToSell > 0) {
            Lot oldestLot = tickerLots.peekFirst();
            int consumed = Math.min(oldestLot.quantity, remainingToSell);
            costBasis = costBasis.add(oldestLot.price.multiply(consumed));
            oldestLot.quantity -= consumed;
            remainingToSell -= consumed;
            if (oldestLot.quantity == 0) {
                tickerLots.pollFirst();
            }
        }

        Money proceeds = price.multiply(quantity);
        cash = cash.add(proceeds);
        return proceeds.subtract(costBasis);
    }
}

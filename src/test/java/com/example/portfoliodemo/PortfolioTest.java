package com.example.portfoliodemo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PortfolioTest {

    @Test
    void newPortfolioHasZeroBalanceByDefault() {
        Portfolio portfolio = new Portfolio();
        assertEquals(Money.of(0), portfolio.cashBalance());
    }

    @Test
    void depositIncreasesCashBalance() {
        Portfolio portfolio = new Portfolio();
        portfolio.deposit(Money.of(1000));
        assertEquals(Money.of(1000), portfolio.cashBalance());
    }

    @Test
    void buyDeductsCashBalance() {
        Portfolio portfolio = new Portfolio(Money.of(1000));
        portfolio.buy("AAPL", 10, Money.of("50.00"));
        assertEquals(Money.of(500), portfolio.cashBalance());
    }

    @Test
    void buyRecordsSharesOwned() {
        Portfolio portfolio = new Portfolio(Money.of(1000));
        portfolio.buy("AAPL", 10, Money.of("50.00"));
        assertEquals(10, portfolio.sharesOwned("AAPL"));
    }

    @Test
    void buyWithInsufficientFundsRaises() {
        Portfolio portfolio = new Portfolio(Money.of(100));
        assertThrows(InsufficientFundsException.class,
                () -> portfolio.buy("AAPL", 10, Money.of("50.00")));
    }

    @Test
    void sellIncreasesCashBalance() {
        Portfolio portfolio = new Portfolio(Money.of(1000));
        portfolio.buy("AAPL", 10, Money.of("50.00"));
        portfolio.sell("AAPL", 10, Money.of("60.00"));
        assertEquals(Money.of(1100), portfolio.cashBalance()); // 500 left + 600 proceeds
    }

    @Test
    void sellReducesSharesOwned() {
        Portfolio portfolio = new Portfolio(Money.of(1000));
        portfolio.buy("AAPL", 10, Money.of("50.00"));
        portfolio.sell("AAPL", 4, Money.of("60.00"));
        assertEquals(6, portfolio.sharesOwned("AAPL"));
    }

    @Test
    void sellMoreThanOwnedRaises() {
        Portfolio portfolio = new Portfolio(Money.of(1000));
        portfolio.buy("AAPL", 5, Money.of("50.00"));
        assertThrows(InsufficientSharesException.class,
                () -> portfolio.sell("AAPL", 10, Money.of("60.00")));
    }

    @Test
    void sellUsesFifoLotsForRealizedGain() {
        Portfolio portfolio = new Portfolio(Money.of(10_000));
        portfolio.buy("AAPL", 10, Money.of("50.00")); // lot 1: cost basis $50/share
        portfolio.buy("AAPL", 10, Money.of("70.00")); // lot 2: cost basis $70/share

        Money realizedGain = portfolio.sell("AAPL", 15, Money.of("80.00"));

        // Sells all 10 shares from lot 1 (@50) plus 5 shares from lot 2 (@70).
        // cost basis = 10*50 + 5*70 = 850 | proceeds = 15*80 = 1200 | gain = 350
        assertEquals(Money.of(350), realizedGain);
    }
}

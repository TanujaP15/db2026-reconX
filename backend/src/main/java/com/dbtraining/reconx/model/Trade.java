package com.dbtraining.reconx.model;

import java.util.Objects;

/* package-private abstract base shared by all concrete trades */
abstract class Trade {
    protected final TradeRef tradeRef;
    protected final Money notional;
    protected final java.time.LocalDate tradeDate;

    Trade(TradeRef tradeRef, Money notional, java.time.LocalDate tradeDate) {
        this.tradeRef = Objects.requireNonNull(tradeRef, "tradeRef");
        this.notional = Objects.requireNonNull(notional, "notional");
        this.tradeDate = Objects.requireNonNull(tradeDate, "tradeDate");
    }
}

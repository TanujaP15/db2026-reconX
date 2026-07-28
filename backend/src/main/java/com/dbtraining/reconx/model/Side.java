package com.dbtraining.reconx.model;

/**
 * ============================================================================
 * Side
 *
 * WHAT:    Enum representing the side of a trade (BUY or SELL).
 *          Used across all TradeType implementations.
 * HOW:     Kept as a tiny enum rather than a String.
 * WHY:     Prevents typos from surviving compilation.
 * ============================================================================
 */
public enum Side {
    BUY, SELL
}

package com.dbtraining.reconx.exception;

/**
 * ============================================================================
 * TradeNotFoundException
 *
 * WHAT:    Thrown when a tradeRef has no corresponding row in the trades table.
 *          Results in a 404 Not Found.
 * HOW:     Extends {@link ReconException}.
 * WHY:     Indicates a lookup failure for a requested trade.
 * ============================================================================
 */
public class TradeNotFoundException extends ReconException {
    /**
     * Constructs a new exception for the missing trade reference.
     * @param tradeRef the trade reference that could not be found
     */
    public TradeNotFoundException(String tradeRef) {
        super("Trade not found: " + tradeRef);
    }
}

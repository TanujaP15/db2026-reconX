package com.dbtraining.reconx.exception;

/**
 * ============================================================================
 * DuplicateTradeRefException
 *
 * WHAT:    Thrown when a tradeRef already exists in the system.
 *          Results in a 409 Conflict.
 * HOW:     Extends {@link ReconException}.
 * WHY:     tradeRef is the natural key; duplicates violate domain invariants.
 * ============================================================================
 */
public class DuplicateTradeRefException extends ReconException {
    /**
     * Constructs a new exception with the duplicated trade reference.
     * @param tradeRef the trade reference that already exists
     */
    public DuplicateTradeRefException(String tradeRef) {
        super("Duplicate tradeRef: " + tradeRef);
    }
}

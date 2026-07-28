package com.dbtraining.reconx.exception;

/**
 * ============================================================================
 * ReconciliationMismatchException
 *
 * WHAT:    Thrown when an internal trade does not match an external trade.
 *          Results in a 422 Unprocessable Entity.
 * HOW:     Extends {@link ReconException}.
 * WHY:     Captures mismatches during the reconciliation process.
 * ============================================================================
 */
public class ReconciliationMismatchException extends ReconException {
    /**
     * Constructs a new exception with the specified detail message.
     * @param message the detail message describing the mismatch
     */
    public ReconciliationMismatchException(String message) { super(message); }
}

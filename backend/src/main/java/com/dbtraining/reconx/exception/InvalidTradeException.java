package com.dbtraining.reconx.exception;

/**
 * ============================================================================
 * InvalidTradeException
 *
 * WHAT:    Thrown when a trade fails business validation (e.g. negative price).
 *          Results in a 400 Bad Request.
 * HOW:     Extends {@link ReconException}.
 * WHY:     Differentiates business validation errors from systemic errors.
 * ============================================================================
 */
public class InvalidTradeException extends ReconException {
    /**
     * Constructs a new exception with the specified detail message.
     * @param message the detail message explaining the validation failure
     */
    public InvalidTradeException(String message) { super(message); }
}

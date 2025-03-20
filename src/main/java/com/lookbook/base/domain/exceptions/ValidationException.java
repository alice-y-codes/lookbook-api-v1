package com.lookbook.base.domain.exceptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Exception thrown when domain validation fails.
 * Used to signal invariant violations in domain objects.
 */
public class ValidationException extends DomainException {

    private final List<String> violations;

    /**
     * Creates a new validation exception with a single violation.
     *
     * @param message The validation error message
     */
    public ValidationException(String message) {
        this(Collections.singletonList(message));
    }

    /**
     * Creates a new validation exception with multiple violations.
     *
     * @param violations A list of validation error messages
     */
    public ValidationException(List<String> violations) {
        super("VAL_ERR", buildMessage(violations));
        this.violations = Collections.unmodifiableList(new ArrayList<>(violations));
    }

    /**
     * Gets the list of validation violations.
     *
     * @return An unmodifiable list of validation error messages
     */
    public List<String> getViolations() {
        return violations;
    }

    /**
     * Builds a composite message from multiple validation violations.
     *
     * @param violations A list of validation error messages
     * @return A combined error message
     */
    private static String buildMessage(List<String> violations) {
        if (violations == null || violations.isEmpty()) {
            return "Validation failed";
        }

        if (violations.size() == 1) {
            return violations.get(0);
        }

        return String.format("Validation failed with %d violations: %s",
                violations.size(),
                String.join("; ", violations));
    }
}
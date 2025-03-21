package com.lookbook.user.domain.aggregates;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.lookbook.base.domain.exceptions.ValidationException;

/**
 * Represents the status of a user in the system.
 * Controls valid state transitions between statuses.
 */
public enum UserStatus {
    /**
     * User is active and can use all system features
     */
    ACTIVE,

    /**
     * User is inactive and cannot log in or use system features
     */
    INACTIVE,

    /**
     * User registration is pending completion/verification
     */
    PENDING;

    // Allowed transitions map - defines valid state transitions for each status
    private static final Map<UserStatus, Set<UserStatus>> ALLOWED_TRANSITIONS;

    static {
        Map<UserStatus, Set<UserStatus>> transitions = new EnumMap<>(UserStatus.class);

        // From PENDING, can go to ACTIVE or INACTIVE
        transitions.put(PENDING, new HashSet<>(Arrays.asList(ACTIVE, INACTIVE)));

        // From ACTIVE, can only go to INACTIVE
        transitions.put(ACTIVE, Collections.singleton(INACTIVE));

        // From INACTIVE, can only go to ACTIVE
        transitions.put(INACTIVE, Collections.singleton(ACTIVE));

        ALLOWED_TRANSITIONS = Collections.unmodifiableMap(transitions);
    }

    /**
     * Checks if a transition from this status to the target status is valid.
     *
     * @param targetStatus The status to transition to
     * @return true if the transition is allowed, false otherwise
     */
    public boolean canTransitionTo(UserStatus targetStatus) {
        // Same status is always allowed (no transition)
        if (this == targetStatus) {
            return true;
        }

        // Check if the transition is allowed
        Set<UserStatus> allowedTargets = ALLOWED_TRANSITIONS.get(this);
        return allowedTargets != null && allowedTargets.contains(targetStatus);
    }

    /**
     * Transitions to the target status if allowed.
     *
     * @param targetStatus The status to transition to
     * @return The target status if transition is allowed
     * @throws ValidationException if the transition is not allowed
     */
    public UserStatus transitionTo(UserStatus targetStatus) {
        if (!canTransitionTo(targetStatus)) {
            throw new ValidationException(
                    String.format("Cannot transition from %s to %s", this, targetStatus));
        }
        return targetStatus;
    }
}
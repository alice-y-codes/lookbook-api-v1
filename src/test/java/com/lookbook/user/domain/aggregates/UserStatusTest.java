package com.lookbook.user.domain.aggregates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.lookbook.base.domain.exceptions.ValidationException;

class UserStatusTest {

    @Test
    void sameStatusTransitionShouldAlwaysBeAllowed() {
        // Given all statuses
        for (UserStatus status : UserStatus.values()) {
            // When-Then: can transition to itself
            assertTrue(status.canTransitionTo(status));
            assertEquals(status, status.transitionTo(status));
        }
    }

    @ParameterizedTest
    @CsvSource({
            "PENDING,ACTIVE,true",
            "PENDING,INACTIVE,true",
            "ACTIVE,INACTIVE,true",
            "INACTIVE,ACTIVE,true",
            "ACTIVE,PENDING,false",
            "INACTIVE,PENDING,false"
    })
    void shouldEnforceValidTransitionRules(String fromStatus, String toStatus, boolean allowed) {
        // Given
        UserStatus from = UserStatus.valueOf(fromStatus);
        UserStatus to = UserStatus.valueOf(toStatus);

        // When-Then
        assertEquals(allowed, from.canTransitionTo(to));
    }

    @Test
    void pendingUserCanBeActivated() {
        // Given
        UserStatus pendingStatus = UserStatus.PENDING;

        // When
        UserStatus result = pendingStatus.transitionTo(UserStatus.ACTIVE);

        // Then
        assertEquals(UserStatus.ACTIVE, result);
    }

    @Test
    void pendingUserCanBeDeactivated() {
        // Given
        UserStatus pendingStatus = UserStatus.PENDING;

        // When
        UserStatus result = pendingStatus.transitionTo(UserStatus.INACTIVE);

        // Then
        assertEquals(UserStatus.INACTIVE, result);
    }

    @Test
    void activeUserCanBeDeactivated() {
        // Given
        UserStatus activeStatus = UserStatus.ACTIVE;

        // When
        UserStatus result = activeStatus.transitionTo(UserStatus.INACTIVE);

        // Then
        assertEquals(UserStatus.INACTIVE, result);
    }

    @Test
    void inactiveUserCanBeActivated() {
        // Given
        UserStatus inactiveStatus = UserStatus.INACTIVE;

        // When
        UserStatus result = inactiveStatus.transitionTo(UserStatus.ACTIVE);

        // Then
        assertEquals(UserStatus.ACTIVE, result);
    }

    @Test
    void activeUserCannotBePending() {
        // Given
        UserStatus activeStatus = UserStatus.ACTIVE;

        // When-Then
        assertFalse(activeStatus.canTransitionTo(UserStatus.PENDING));
        assertThrows(ValidationException.class,
                () -> activeStatus.transitionTo(UserStatus.PENDING));
    }

    @Test
    void inactiveUserCannotBePending() {
        // Given
        UserStatus inactiveStatus = UserStatus.INACTIVE;

        // When-Then
        assertFalse(inactiveStatus.canTransitionTo(UserStatus.PENDING));
        assertThrows(ValidationException.class,
                () -> inactiveStatus.transitionTo(UserStatus.PENDING));
    }
}
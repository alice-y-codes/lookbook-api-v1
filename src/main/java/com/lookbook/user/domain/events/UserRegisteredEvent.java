package com.lookbook.user.domain.events;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import com.lookbook.base.domain.events.BaseDomainEvent;

import lombok.Getter;

/**
 * Event raised when a new user registers.
 */
@Getter
public class UserRegisteredEvent extends BaseDomainEvent {
    private final UUID userId;
    private final String username;
    private final String email;

    public UserRegisteredEvent(UUID userId, String username, String email) {
        // Store all fields in metadata for proper event comparison
        super(Map.of(
                "userId", userId,
                "username", username,
                "email", email));
        this.userId = userId;
        this.username = username;
        this.email = email;
    }

    @Override
    public UUID getAggregateId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        UserRegisteredEvent that = (UserRegisteredEvent) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(username, that.username) &&
                Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId, username, email);
    }
}
package com.lookbook.user.infrastructure.persistence.entities;

import org.hibernate.annotations.Comment;

import com.lookbook.base.infrastructure.persistence.entities.JpaBaseEntity;
import com.lookbook.user.domain.aggregates.User;
import com.lookbook.user.domain.aggregates.UserStatus;
import com.lookbook.user.domain.valueobjects.Email;
import com.lookbook.user.domain.valueobjects.Username;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * JPA entity that maps the User domain entity to the database.
 * This class handles the persistence concerns while keeping the domain model
 * clean.
 */
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_users_username", columnList = "username"),
        @Index(name = "idx_users_email", columnList = "email"),
        @Index(name = "idx_users_status", columnList = "status")
})
public class JpaUser extends JpaBaseEntity {

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    @Comment("BCrypt hash of the user password (includes salt)")
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status;

    /**
     * Default constructor required by JPA.
     */
    protected JpaUser() {
        // Required by JPA
    }

    /**
     * Creates a new JpaUser from a User domain entity.
     *
     * @param user The domain entity to map
     */
    public JpaUser(User user) {
        super(user);
        this.username = user.getUsername().getValue();
        this.email = user.getEmail().getValue();
        this.passwordHash = user.getHashedPassword();
        this.status = user.getStatus();
    }

    /**
     * Converts this JPA entity back to a domain User entity.
     *
     * @return The domain User entity
     */
    public User toDomainEntity() {
        return User.reconstitute(
                getId(),
                Username.of(username),
                Email.of(email),
                passwordHash,
                status,
                getCreatedAt(),
                getUpdatedAt());
    }

    // Getters and setters

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public UserStatus getStatus() {
        return status;
    }

    protected void setUsername(String username) {
        this.username = username;
    }

    protected void setEmail(String email) {
        this.email = email;
    }

    protected void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    protected void setStatus(UserStatus status) {
        this.status = status;
    }
}
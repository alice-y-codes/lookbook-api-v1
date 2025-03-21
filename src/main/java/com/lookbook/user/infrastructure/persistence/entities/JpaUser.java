package com.lookbook.user.infrastructure.persistence.entities;

import com.lookbook.base.infrastructure.persistence.entities.JpaBaseEntity;
import com.lookbook.user.domain.aggregates.User;
import com.lookbook.user.domain.aggregates.UserStatus;
import com.lookbook.user.domain.valueobjects.Email;
import com.lookbook.user.domain.valueobjects.Username;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

/**
 * JPA entity that maps the User domain entity to the database.
 * This class handles the persistence concerns while keeping the domain model
 * clean.
 */
@Entity
@Table(name = "users")
public class JpaUser extends JpaBaseEntity {

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "password_salt", nullable = false)
    private String passwordSalt;

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
        this.passwordSalt = user.getPasswordSalt();
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
                passwordSalt,
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

    public String getPasswordSalt() {
        return passwordSalt;
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

    protected void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    protected void setStatus(UserStatus status) {
        this.status = status;
    }
}
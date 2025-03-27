package com.lookbook.user.infrastructure.persistence.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import com.lookbook.base.infrastructure.persistence.entities.JpaBaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * JPA entity for Profile.
 */
@Entity
@Table(name = "profiles")
@Getter
@Setter
public class JpaProfile extends JpaBaseEntity {
    private UUID userId;
    private String displayName;
    private String biography;
    private String profileImageUrl; // TODO: Add proper profile image handling
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
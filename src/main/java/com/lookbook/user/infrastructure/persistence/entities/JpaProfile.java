package com.lookbook.user.infrastructure.persistence.entities;

import java.util.UUID;

import com.lookbook.base.infrastructure.persistence.entities.JpaBaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * JPA entity for Profile.
 */
@Entity
@Table(name = "profiles", indexes = {
        @Index(name = "idx_profiles_user_id", columnList = "userId")
})
@Getter
@Setter
public class JpaProfile extends JpaBaseEntity {
    private UUID userId;
    private String displayName;
    private String biography;
    private String profileImageUrl; // TODO: Add proper profile image handling
}
package com.lookbook.user.infrastructure.persistence.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.lookbook.base.infrastructure.persistence.repositories.JpaEntityRepositoryAdapter;
import com.lookbook.user.domain.aggregates.UserProfile;
import com.lookbook.user.domain.repositories.ProfileRepository;
import com.lookbook.user.domain.valueobjects.Biography;
import com.lookbook.user.domain.valueobjects.DisplayName;
import com.lookbook.user.infrastructure.persistence.entities.JpaProfile;

/**
 * Implementation of the ProfileRepository interface using Spring Data JPA.
 * Extends the generic JpaEntityRepositoryAdapter for common CRUD and time-based
 * operations.
 */
@Component
public class ProfileRepositoryAdapter extends JpaEntityRepositoryAdapter<UserProfile, JpaProfile>
        implements ProfileRepository {

    private final JpaProfileRepository profileRepository;

    public ProfileRepositoryAdapter(JpaProfileRepository profileRepository) {
        super(profileRepository);
        this.profileRepository = profileRepository;
    }

    @Override
    public Optional<UserProfile> findByUserId(UUID userId) {
        return profileRepository.findByUserId(userId)
                .map(this::mapToEntity);
    }

    @Override
    protected UserProfile mapToEntity(JpaProfile jpaEntity) {
        return UserProfile.reconstitute(
                jpaEntity.getId(),
                jpaEntity.getUserId(),
                DisplayName.of(jpaEntity.getDisplayName()),
                Biography.of(jpaEntity.getBiography()),
                null, // profileImage is not stored in JPA entity yet
                jpaEntity.getCreatedAt(),
                jpaEntity.getUpdatedAt());
    }

    @Override
    protected JpaProfile mapToJpaEntity(UserProfile domainEntity) {
        JpaProfile jpaEntity = new JpaProfile();
        jpaEntity.setUserId(domainEntity.getUserId());
        jpaEntity.setDisplayName(domainEntity.getDisplayName().getValue());
        jpaEntity.setBiography(domainEntity.getBiography().getValue());
        return jpaEntity;
    }
}
package com.lookbook.base.infrastructure.persistence.entities;

import com.lookbook.base.domain.entities.TestEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "test_entities")
public class JpaTestEntity extends JpaBaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    protected JpaTestEntity() {
        // Required by JPA
    }

    public JpaTestEntity(TestEntity entity) {
        super(entity);
        this.name = entity.getName();
    }

    public String getName() {
        return name;
    }

    public TestEntity toDomainEntity() {
        return new TestEntity(getId(), getCreatedAt(), getUpdatedAt(), name);
    }
}
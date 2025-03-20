package com.lookbook.base.domain.entities;

import java.time.LocalDateTime;
import java.util.UUID;

public class TestEntity extends BaseEntity {
    private final String name;

    public TestEntity(String name) {
        super();
        this.name = name;
    }

    public TestEntity(UUID id, LocalDateTime createdAt, LocalDateTime updatedAt, String name) {
        super(id, createdAt, updatedAt);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
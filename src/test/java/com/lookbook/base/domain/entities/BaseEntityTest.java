package com.lookbook.base.domain.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link BaseEntity} class.
 */
class BaseEntityTest {

    /**
     * Concrete implementation of BaseEntity for testing.
     */
    private static class TestEntity extends BaseEntity {

        private String name;

        public TestEntity() {
            super();
            this.name = "Default";
        }

        public TestEntity(UUID id, LocalDateTime createdAt, LocalDateTime updatedAt) {
            super(id, createdAt, updatedAt);
            this.name = "Default";
        }

        public void changeName(String newName) {
            this.name = newName;
            markUpdated();
        }

        public String getName() {
            return name;
        }
    }

    @Test
    void constructor_Default_ShouldGenerateIdAndTimestamps() {
        TestEntity entity = new TestEntity();

        assertNotNull(entity.getId());
        assertNotNull(entity.getCreatedAt());
        assertNotNull(entity.getUpdatedAt());
        assertEquals(entity.getCreatedAt(), entity.getUpdatedAt());

        // Ensure timestamp is recent
        LocalDateTime now = LocalDateTime.now();
        long secondsDiff = ChronoUnit.SECONDS.between(entity.getCreatedAt(), now);
        assertTrue(secondsDiff < 5, "Creation timestamp should be within 5 seconds of now");
    }

    @Test
    void constructor_WithParameters_ShouldUseProvidedValues() {
        UUID id = UUID.randomUUID();
        LocalDateTime created = LocalDateTime.now().minusDays(5);
        LocalDateTime updated = LocalDateTime.now().minusHours(1);

        TestEntity entity = new TestEntity(id, created, updated);

        assertEquals(id, entity.getId());
        assertEquals(created, entity.getCreatedAt());
        assertEquals(updated, entity.getUpdatedAt());
    }

    @Test
    void constructor_WithNullId_ShouldThrowNullPointerException() {
        LocalDateTime timestamp = LocalDateTime.now();

        assertThrows(NullPointerException.class, () -> new TestEntity(null, timestamp, timestamp));
    }

    @Test
    void constructor_WithNullCreatedAt_ShouldThrowNullPointerException() {
        UUID id = UUID.randomUUID();
        LocalDateTime timestamp = LocalDateTime.now();

        assertThrows(NullPointerException.class, () -> new TestEntity(id, null, timestamp));
    }

    @Test
    void constructor_WithNullUpdatedAt_ShouldThrowNullPointerException() {
        UUID id = UUID.randomUUID();
        LocalDateTime timestamp = LocalDateTime.now();

        assertThrows(NullPointerException.class, () -> new TestEntity(id, timestamp, null));
    }

    @Test
    void markUpdated_ShouldUpdateTimestamp() throws InterruptedException {
        TestEntity entity = new TestEntity();
        LocalDateTime originalUpdateTime = entity.getUpdatedAt();

        // Wait briefly to ensure time difference
        Thread.sleep(10);

        entity.changeName("New Name");
        LocalDateTime newUpdateTime = entity.getUpdatedAt();

        assertTrue(newUpdateTime.isAfter(originalUpdateTime),
                "Updated timestamp should be later than original timestamp");
    }

    @Test
    void equals_SameEntity_ShouldBeEqual() {
        TestEntity entity = new TestEntity();

        assertEquals(entity, entity);
    }

    @Test
    void equals_EntitiesWithSameId_ShouldBeEqual() {
        UUID id = UUID.randomUUID();
        LocalDateTime created1 = LocalDateTime.now().minusDays(2);
        LocalDateTime updated1 = LocalDateTime.now().minusHours(2);

        LocalDateTime created2 = LocalDateTime.now().minusDays(1);
        LocalDateTime updated2 = LocalDateTime.now().minusHours(1);

        TestEntity entity1 = new TestEntity(id, created1, updated1);
        TestEntity entity2 = new TestEntity(id, created2, updated2);

        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    void equals_EntitiesWithDifferentIds_ShouldNotBeEqual() {
        TestEntity entity1 = new TestEntity();
        TestEntity entity2 = new TestEntity();

        assertNotEquals(entity1, entity2);
        assertNotEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    void equals_EntityAndNull_ShouldNotBeEqual() {
        TestEntity entity = new TestEntity();

        assertNotEquals(entity, null);
    }

    @Test
    void equals_EntityAndDifferentType_ShouldNotBeEqual() {
        TestEntity entity = new TestEntity();
        String someString = "Not an entity";

        assertNotEquals(entity, someString);
    }
}
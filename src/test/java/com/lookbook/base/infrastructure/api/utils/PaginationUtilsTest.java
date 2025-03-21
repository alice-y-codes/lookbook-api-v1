package com.lookbook.base.infrastructure.api.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.lookbook.base.infrastructure.api.response.ApiResponse;

class PaginationUtilsTest {

    @Test
    void createPageRequest_withDefaults_shouldUseDefaultValues() {
        // When
        Pageable pageable = PaginationUtils.createPageRequest(null, null, null, null);

        // Then
        assertEquals(0, pageable.getPageNumber());
        assertEquals(20, pageable.getPageSize());
        assertNotNull(pageable.getSort());
        assertEquals(Sort.unsorted(), pageable.getSort());
    }

    @Test
    void createPageRequest_withCustomValues_shouldUseProvidedValues() {
        // When
        Pageable pageable = PaginationUtils.createPageRequest(2, 10, "name", "asc");

        // Then
        assertEquals(2, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());
        assertNotNull(pageable.getSort());
        assertEquals(Sort.by(Direction.ASC, "name"), pageable.getSort());
    }

    @Test
    void createPageRequest_withTooLargePageSize_shouldCapPageSize() {
        // When
        Pageable pageable = PaginationUtils.createPageRequest(0, 1000, null, null);

        // Then
        assertEquals(0, pageable.getPageNumber());
        assertEquals(100, pageable.getPageSize()); // Should cap at max page size
    }

    @Test
    void createPageRequest_withNegativePageNumber_shouldUseZero() {
        // When
        Pageable pageable = PaginationUtils.createPageRequest(-5, 10, null, null);

        // Then
        assertEquals(0, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());
    }

    @Test
    void validateSortParams_withValidField_shouldNotThrowException() {
        // Given
        Set<String> allowedFields = new HashSet<>(Arrays.asList("name", "email", "createdAt"));

        // When & Then
        // No exception should be thrown
        boolean isValid = PaginationUtils.validateSortParams("name", allowedFields);
        assertTrue(isValid);
    }

    @Test
    void validateSortParams_withInvalidField_shouldReturnFalse() {
        // Given
        Set<String> allowedFields = new HashSet<>(Arrays.asList("name", "email", "createdAt"));

        // When & Then
        boolean isValid = PaginationUtils.validateSortParams("invalidField", allowedFields);
        assertTrue(!isValid);
    }

    @Test
    void createPageResponse_shouldCreateApiResponseWithPageMetadata() {
        // Given
        List<String> items = Arrays.asList("Item 1", "Item 2", "Item 3");
        Page<String> page = new PageImpl<>(items, PageRequest.of(0, 10), 25);

        // When
        ApiResponse<List<String>> response = PaginationUtils.createPageResponse(page);

        // Then
        assertEquals("success", response.getStatus());
        assertEquals(items, response.getData());
        assertNotNull(response.getMeta());

        @SuppressWarnings("unchecked")
        Map<String, Object> pagination = (Map<String, Object>) response.getMeta().get("pagination");
        assertNotNull(pagination);
        assertEquals(0, pagination.get("page"));
        assertEquals(10, pagination.get("size"));
        assertEquals(25L, pagination.get("totalElements"));
        assertEquals(3, pagination.get("totalPages"));
        assertEquals(true, pagination.get("first"));
        assertEquals(false, pagination.get("last"));
    }

    @Test
    void createPageResponse_withLastPage_shouldSetLastToTrue() {
        // Given
        List<String> items = Arrays.asList("Item 21", "Item 22", "Item 23", "Item 24", "Item 25");
        Page<String> page = new PageImpl<>(items, PageRequest.of(2, 10), 25);

        // When
        ApiResponse<List<String>> response = PaginationUtils.createPageResponse(page);

        // Then
        @SuppressWarnings("unchecked")
        Map<String, Object> pagination = (Map<String, Object>) response.getMeta().get("pagination");
        assertNotNull(pagination);
        assertEquals(2, pagination.get("page"));
        assertEquals(10, pagination.get("size"));
        assertEquals(false, pagination.get("first"));
        assertEquals(true, pagination.get("last"));
    }
}
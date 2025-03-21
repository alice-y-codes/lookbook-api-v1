package com.lookbook.base.infrastructure.api.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.lookbook.base.infrastructure.api.response.ApiResponse;

/**
 * Utility methods for API pagination handling.
 */
public final class PaginationUtils {

    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;
    private static final int DEFAULT_PAGE = 0;

    private PaginationUtils() {
        // Prevent instantiation
    }

    /**
     * Creates a Spring PageRequest from API request parameters.
     * 
     * @param page      The requested page (0-indexed)
     * @param size      The requested page size
     * @param sort      The sort field
     * @param direction The sort direction
     * @return A PageRequest object
     */
    public static Pageable createPageRequest(Integer page, Integer size, String sort, String direction) {
        int pageNumber = (page != null && page >= 0) ? page : DEFAULT_PAGE;
        int pageSize = calculatePageSize(size);

        if (sort != null && !sort.isEmpty()) {
            Direction sortDirection = Direction.fromOptionalString(direction)
                    .orElse(Direction.ASC);

            return PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, sort));
        }

        return PageRequest.of(pageNumber, pageSize);
    }

    /**
     * Validates sort parameters against a list of allowed fields.
     * 
     * @param sort          The requested sort field
     * @param allowedFields Set of allowed field names
     * @return true if the sort field is allowed, false otherwise
     */
    public static boolean validateSortParams(String sort, Set<String> allowedFields) {
        return sort == null || sort.isEmpty() || allowedFields.contains(sort);
    }

    /**
     * Creates a paginated API response from a Spring Page object.
     * 
     * @param <T>  The entity type
     * @param page The Spring Page
     * @return An API response with pagination metadata
     */
    public static <T> ApiResponse<List<T>> createPageResponse(Page<T> page) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("page", page.getNumber());
        metadata.put("size", page.getSize());
        metadata.put("totalElements", page.getTotalElements());
        metadata.put("totalPages", page.getTotalPages());
        metadata.put("first", page.isFirst());
        metadata.put("last", page.isLast());

        if (page.getSort() != null && page.getSort().isSorted()) {
            Order order = page.getSort().stream().findFirst().orElse(null);
            if (order != null) {
                metadata.put("sort", order.getProperty());
                metadata.put("direction", order.getDirection().name());
            }
        }

        return ApiResponse.success(page.getContent())
                .addMetadata("pagination", metadata);
    }

    /**
     * Calculates the page size based on the requested size, enforcing limits.
     * 
     * @param size The requested page size
     * @return The validated page size
     */
    private static int calculatePageSize(Integer size) {
        if (size == null) {
            return DEFAULT_PAGE_SIZE;
        }

        if (size <= 0) {
            return DEFAULT_PAGE_SIZE;
        }

        return Math.min(size, MAX_PAGE_SIZE);
    }
}
package com.lookbook.base.infrastructure.api.controllers;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.lookbook.base.domain.exceptions.DomainException;
import com.lookbook.base.infrastructure.api.response.ApiResponse;
import com.lookbook.base.infrastructure.api.response.ErrorResponse;
import com.lookbook.base.infrastructure.api.utils.PaginationUtils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Base controller providing common functionality for all REST controllers.
 */
public abstract class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Creates a success response with data.
     * 
     * @param <T>  The data type
     * @param data The response data
     * @return A success response with the data
     */
    protected <T> ApiResponse<T> createSuccessResponse(T data) {
        return ApiResponse.success(data);
    }

    /**
     * Creates a success response with data and message.
     * 
     * @param <T>     The data type
     * @param data    The response data
     * @param message A success message
     * @return A success response with data and message
     */
    protected <T> ApiResponse<T> createSuccessResponse(T data, String message) {
        logger.debug("Creating success response with data: {} and message: {}", data, message);
        ApiResponse<T> response = ApiResponse.success(data, message);
        logger.debug("Created API response: {}", response);
        return response;
    }

    /**
     * Creates an error response.
     * 
     * @param code    The error code
     * @param message The error message
     * @param request The HTTP request
     * @return An error response
     */
    protected ErrorResponse createErrorResponse(String code, String message, HttpServletRequest request) {
        return ErrorResponse.of(code, message, getRequestPath(request));
    }

    /**
     * Creates a paginated response from a Spring Page.
     * 
     * @param <T>  The entity type
     * @param page The Spring Page object
     * @return A paginated API response
     */
    protected <T> ApiResponse<List<T>> createPageResponse(Page<T> page) {
        return PaginationUtils.createPageResponse(page);
    }

    /**
     * Gets the current authenticated user, if any.
     * 
     * @return The authentication object or null if not authenticated
     */
    protected Authentication getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Gets the request path for error responses.
     * 
     * @param request The HTTP request
     * @return The request path
     */
    protected String getRequestPath(HttpServletRequest request) {
        return request.getRequestURI();
    }

    /**
     * Checks if pagination is requested in the request.
     * 
     * @param page The page parameter
     * @param size The size parameter
     * @return true if pagination is requested, false otherwise
     */
    protected boolean isPaginationRequested(Integer page, Integer size) {
        return page != null || size != null;
    }

    /**
     * Creates a PageRequest from request parameters with validation.
     * 
     * @param page              The requested page (0-indexed)
     * @param size              The requested page size
     * @param sort              The sort field
     * @param direction         The sort direction
     * @param allowedSortFields Set of allowed sort fields
     * @return A validated PageRequest
     * @throws DomainException if sort validation fails
     */
    protected Pageable createPageRequest(
            Integer page,
            Integer size,
            String sort,
            String direction,
            Set<String> allowedSortFields) throws DomainException {

        if (sort != null && !allowedSortFields.isEmpty()
                && !PaginationUtils.validateSortParams(sort, allowedSortFields)) {
            throw new IllegalArgumentException("Invalid sort field: " + sort);
        }

        return PaginationUtils.createPageRequest(page, size, sort, direction);
    }

    // Common response status annotations for controller methods

    @ResponseStatus(HttpStatus.OK)
    protected @interface ResponseOk {
    }

    @ResponseStatus(HttpStatus.CREATED)
    protected @interface ResponseCreated {
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    protected @interface ResponseNoContent {
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected @interface ResponseBadRequest {
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected @interface ResponseNotFound {
    }
}
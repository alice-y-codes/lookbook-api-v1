package com.lookbook.base.infrastructure.web.response;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Generic wrapper for all API responses.
 * 
 * @param <T> The type of data contained in the response
 */
@JsonInclude(Include.NON_NULL)
public class ApiResponse<T> {

    private String status;
    private T data;
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime timestamp;

    @JsonInclude(Include.NON_EMPTY)
    private Map<String, Object> meta;

    protected ApiResponse() {
        this.timestamp = LocalDateTime.now();
        this.meta = new HashMap<>();
    }

    public static <T> ApiResponse<T> success() {
        return success(null, null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return success(data, null);
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.status = "success";
        response.data = data;
        response.message = message;
        return response;
    }

    public static <T> ApiResponse<T> error(String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.status = "error";
        response.message = message;
        return response;
    }

    public ApiResponse<T> addMetadata(String key, Object value) {
        this.meta.put(key, value);
        return this;
    }

    // Getters and setters

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, Object> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, Object> meta) {
        this.meta = meta;
    }
}
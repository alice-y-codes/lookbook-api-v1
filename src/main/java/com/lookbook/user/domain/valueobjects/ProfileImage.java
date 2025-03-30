package com.lookbook.user.domain.valueobjects;

import java.net.URI;
import java.util.Objects;

import com.lookbook.base.domain.exceptions.ValidationException;
import com.lookbook.base.domain.validation.ValidationResult;
import com.lookbook.base.domain.valueobjects.BaseValueObject;

/**
 * Value object representing a user's profile image.
 * Contains metadata about the image including its URL, dimensions, and format.
 */
public class ProfileImage extends BaseValueObject<ProfileImage> {
    private static final int MIN_WIDTH = 100;
    private static final int MIN_HEIGHT = 100;
    private static final int MAX_WIDTH = 2000;
    private static final int MAX_HEIGHT = 2000;
    private static final long MAX_SIZE_BYTES = 5 * 1024 * 1024; // 5MB
    private static final String[] ALLOWED_FORMATS = { "jpg", "jpeg", "png", "gif" };

    private final URI url;
    private final int width;
    private final int height;
    private final String format;
    private final long sizeBytes;

    private ProfileImage(URI url, int width, int height, String format, long sizeBytes) {
        super();
        this.url = url;
        this.width = width;
        this.height = height;
        this.format = format;
        this.sizeBytes = sizeBytes;
        validateSelf();
    }

    /**
     * Creates a new ProfileImage from its components.
     *
     * @param url       the image URL
     * @param width     the image width in pixels
     * @param height    the image height in pixels
     * @param format    the image format (e.g., "jpg", "png")
     * @param sizeBytes the image size in bytes
     * @return a new ProfileImage instance
     * @throws ValidationException if any parameter is invalid
     */
    public static ProfileImage of(URI url, int width, int height, String format, long sizeBytes) {
        if (url == null) {
            throw new ValidationException("Profile image URL cannot be null");
        }

        if (width < MIN_WIDTH || width > MAX_WIDTH) {
            throw new ValidationException(
                    String.format("Image width must be between %d and %d pixels", MIN_WIDTH, MAX_WIDTH));
        }

        if (height < MIN_HEIGHT || height > MAX_HEIGHT) {
            throw new ValidationException(
                    String.format("Image height must be between %d and %d pixels", MIN_HEIGHT, MAX_HEIGHT));
        }

        if (format == null || format.trim().isEmpty()) {
            throw new ValidationException("Image format cannot be empty");
        }

        String trimmedFormat = format.trim().toLowerCase();
        boolean validFormat = false;
        for (String allowedFormat : ALLOWED_FORMATS) {
            if (trimmedFormat.equals(allowedFormat)) {
                validFormat = true;
                break;
            }
        }
        if (!validFormat) {
            throw new ValidationException(
                    String.format("Image format must be one of: %s", String.join(", ", ALLOWED_FORMATS)));
        }

        if (sizeBytes <= 0 || sizeBytes > MAX_SIZE_BYTES) {
            throw new ValidationException(
                    String.format("Image size must be between 1 and %d bytes", MAX_SIZE_BYTES));
        }

        return new ProfileImage(url, width, height, trimmedFormat, sizeBytes);
    }

    @Override
    public ValidationResult validate() {
        ValidationResult result = ValidationResult.valid();

        if (url == null) {
            return result.addError("Profile image URL cannot be null");
        }

        if (width < MIN_WIDTH || width > MAX_WIDTH) {
            result = result.addError(
                    String.format("Image width must be between %d and %d pixels", MIN_WIDTH, MAX_WIDTH));
        }

        if (height < MIN_HEIGHT || height > MAX_HEIGHT) {
            result = result.addError(
                    String.format("Image height must be between %d and %d pixels", MIN_HEIGHT, MAX_HEIGHT));
        }

        if (format == null || format.trim().isEmpty()) {
            result = result.addError("Image format cannot be empty");
        } else {
            boolean validFormat = false;
            for (String allowedFormat : ALLOWED_FORMATS) {
                if (format.toLowerCase().equals(allowedFormat)) {
                    validFormat = true;
                    break;
                }
            }
            if (!validFormat) {
                result = result.addError(
                        String.format("Image format must be one of: %s", String.join(", ", ALLOWED_FORMATS)));
            }
        }

        if (sizeBytes <= 0 || sizeBytes > MAX_SIZE_BYTES) {
            result = result.addError(
                    String.format("Image size must be between 1 and %d bytes", MAX_SIZE_BYTES));
        }

        return result;
    }

    public URI getUrl() {
        return url;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getFormat() {
        return format;
    }

    public long getSizeBytes() {
        return sizeBytes;
    }

    @Override
    protected boolean valueEquals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ProfileImage that = (ProfileImage) o;
        return width == that.width &&
                height == that.height &&
                sizeBytes == that.sizeBytes &&
                Objects.equals(url, that.url) &&
                Objects.equals(format, that.format);
    }

    @Override
    protected int valueHashCode() {
        return Objects.hash(url, width, height, format, sizeBytes);
    }
}
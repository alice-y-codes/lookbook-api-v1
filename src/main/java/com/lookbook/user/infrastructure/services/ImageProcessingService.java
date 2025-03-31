package com.lookbook.user.infrastructure.services;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lookbook.base.domain.exceptions.ValidationException;

import lombok.extern.slf4j.Slf4j;

/**
 * Service for processing and validating images.
 */
@Slf4j
@Service
public class ImageProcessingService {

    private static final int MIN_WIDTH = 100;
    private static final int MIN_HEIGHT = 100;
    private static final int MAX_WIDTH = 2000;
    private static final int MAX_HEIGHT = 2000;
    private static final long MAX_SIZE_BYTES = 5 * 1024 * 1024; // 5MB
    private static final String[] ALLOWED_FORMATS = { "jpg", "jpeg", "png", "gif" };

    /**
     * Validates an image file.
     *
     * @param file the image file to validate
     * @throws ValidationException if the image is invalid
     */
    public void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ValidationException("No file provided");
        }

        // Check file size
        if (file.getSize() > MAX_SIZE_BYTES) {
            throw new ValidationException("File size exceeds maximum limit of 5MB");
        }

        // Check content type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new ValidationException("File must be an image");
        }

        // Check format
        String format = contentType.split("/")[1].toLowerCase();
        boolean formatAllowed = false;
        for (String allowedFormat : ALLOWED_FORMATS) {
            if (format.equals(allowedFormat)) {
                formatAllowed = true;
                break;
            }
        }
        if (!formatAllowed) {
            throw new ValidationException("Image format not supported. Supported formats: " +
                    String.join(", ", ALLOWED_FORMATS));
        }

        // Check dimensions
        try (InputStream is = file.getInputStream()) {
            BufferedImage img = ImageIO.read(is);
            if (img == null) {
                throw new ValidationException("Invalid image file");
            }

            int width = img.getWidth();
            int height = img.getHeight();

            if (width < MIN_WIDTH || width > MAX_WIDTH) {
                throw new ValidationException("Image width must be between " + MIN_WIDTH +
                        " and " + MAX_WIDTH + " pixels");
            }

            if (height < MIN_HEIGHT || height > MAX_HEIGHT) {
                throw new ValidationException("Image height must be between " + MIN_HEIGHT +
                        " and " + MAX_HEIGHT + " pixels");
            }

        } catch (IOException e) {
            log.error("Failed to process image", e);
            throw new ValidationException("Failed to process image: " + e.getMessage());
        }
    }

    /**
     * Gets the dimensions of an image.
     *
     * @param file the image file
     * @return an array containing [width, height]
     */
    public int[] getImageDimensions(MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            BufferedImage img = ImageIO.read(is);
            if (img == null) {
                throw new ValidationException("Invalid image file");
            }
            return new int[] { img.getWidth(), img.getHeight() };
        } catch (IOException e) {
            log.error("Failed to get image dimensions", e);
            throw new ValidationException("Failed to get image dimensions: " + e.getMessage());
        }
    }
}
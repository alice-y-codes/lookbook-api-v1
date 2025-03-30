package com.lookbook.user.infrastructure.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.lookbook.base.domain.exceptions.StorageException;
import com.lookbook.user.domain.repositories.FileStorageRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of FileStorageRepository that stores files in the local
 * filesystem.
 */
@Slf4j
@Component
public class LocalFileStorageAdapter implements FileStorageRepository {

    @Value("${app.storage.local.base-path}")
    private String basePath;

    @Override
    public URI storeFile(InputStream fileStream, String fileName, String contentType) {
        try {
            Path targetPath = Paths.get(basePath, fileName);

            // Create parent directories if they don't exist
            Files.createDirectories(targetPath.getParent());

            // Copy the file
            Files.copy(fileStream, targetPath, StandardCopyOption.REPLACE_EXISTING);

            // Return the file URI
            return targetPath.toUri();

        } catch (IOException e) {
            log.error("Failed to store file: {}", fileName, e);
            throw new StorageException("Failed to store file: " + fileName, e);
        }
    }

    @Override
    public void deleteFile(URI fileUri) {
        try {
            Path filePath = Paths.get(fileUri);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.error("Failed to delete file: {}", fileUri, e);
            throw new StorageException("Failed to delete file: " + fileUri, e);
        }
    }

    @Override
    public boolean fileExists(URI fileUri) {
        try {
            Path filePath = Paths.get(fileUri);
            return Files.exists(filePath);
        } catch (Exception e) {
            log.error("Failed to check file existence: {}", fileUri, e);
            return false;
        }
    }
}
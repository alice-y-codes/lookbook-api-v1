package com.lookbook.user.domain.repositories;

import java.io.InputStream;
import java.net.URI;

/**
 * Repository interface for managing file storage operations.
 * This interface abstracts the storage of files (like profile images) in
 * various storage backends (local filesystem, S3, etc.).
 */
public interface FileStorageRepository {
    /**
     * Store a file in the storage backend.
     *
     * @param fileStream  the input stream containing the file data
     * @param fileName    the name to give the file
     * @param contentType the MIME type of the file
     * @return the URI where the file can be accessed
     */
    URI storeFile(InputStream fileStream, String fileName, String contentType);

    /**
     * Delete a file from the storage backend.
     *
     * @param fileUri the URI of the file to delete
     */
    void deleteFile(URI fileUri);

    /**
     * Check if a file exists in the storage backend.
     *
     * @param fileUri the URI of the file to check
     * @return true if the file exists, false otherwise
     */
    boolean fileExists(URI fileUri);
}
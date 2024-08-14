package com.example.dexport.storage;

import com.example.dexport.aop.DexportStorageException;
import com.example.dexport.config.DexportStorageConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Amirmohammad Khanalipour
 */
public class DexportFileStorageDefault implements DexportFileStorage {
    Logger log = LoggerFactory.getLogger(DexportFileStorageDefault.class);
    private final DexportStorageConfig dexportStorageConfig;

    public DexportFileStorageDefault() {
        this.dexportStorageConfig = new DexportStorageConfig();
        createDownloadsDirectoryIfNotExists();
    }

    public DexportFileStorageDefault(DexportStorageConfig dexportStorageConfig) {
        this.dexportStorageConfig = dexportStorageConfig;
        createDownloadsDirectoryIfNotExists();
    }

    @Override
    public Path getFilePath(String requestId, String extension) {
        return Paths.get(dexportStorageConfig.getSavingDir(), requestId + "." + extension);
    }

    @Override
    public OutputStream getOutputStream(Path filePath) throws IOException {
        createParentDirectoryIfNotExists(filePath);
        return Files.newOutputStream(filePath);
    }

    private void createParentDirectoryIfNotExists(Path filePath) {
        Path parentDir = filePath.getParent();
        if (parentDir != null && !Files.exists(parentDir)) {
            try {
                Files.createDirectories(parentDir);
                log.info("Created parent directory at: {}", parentDir.toAbsolutePath());
            } catch (IOException e) {
                log.error("Failed to create parent directory: {}", parentDir.toAbsolutePath(), e);
                throw new DexportStorageException("Error creating parent directory", e); // Rethrow to propagate the error
            }
        }
    }

    private void createDownloadsDirectoryIfNotExists() {
        Path savingDir = Paths.get(dexportStorageConfig.getSavingDir());
        if (Files.exists(savingDir)) {
            log.info("Saving directory already exists: {}", savingDir.toAbsolutePath());
        } else {
            try {
                Files.createDirectories(savingDir);
                log.info("Created saving directory at: {}", savingDir.toAbsolutePath());
            } catch (IOException e) {
                log.error("Failed to create saving directory: {}", savingDir.toAbsolutePath(), e);
                throw new DexportStorageException("Could not create saving directory", e);
            }
        }
    }
}


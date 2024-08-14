package com.example.dexport.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author Amirmohammad Khanalipour
 */
public class ExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ExceptionHandler.class);

    public void handleException(Exception e) {
        if (e instanceof DexportFileProcessingException) {
            handleFileProcessingException((DexportFileProcessingException) e);
        } else if (e instanceof DexportStorageException) {
            handleStorageException((DexportStorageException) e);
        } else if (e instanceof DexportUnsupportedFormatException) {
            handleUnsupportedFormatException((DexportUnsupportedFormatException) e);
        } else {
            handleGenericException(e);
        }
    }

    private void handleFileProcessingException(DexportFileProcessingException e) {
        log.error("File processing error: {}", e.getMessage(), e);
    }

    private void handleStorageException(DexportStorageException e) {
        log.error("Storage error: {}", e.getMessage(), e);
    }

    private void handleUnsupportedFormatException(DexportUnsupportedFormatException e) {
        log.error("Unsupported format error: {}", e.getMessage(), e);
    }

    private void handleGenericException(Exception e) {
        log.error("An unexpected error occurred: {}", e.getMessage(), e);
    }
}

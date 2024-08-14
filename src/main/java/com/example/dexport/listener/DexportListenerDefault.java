package com.example.dexport.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Amirmohammad Khanalipour
 */
public class DexportListenerDefault implements DexportListener {
    Logger log = LoggerFactory.getLogger(DexportListenerDefault.class);

    @Override
    public void onFinish(String filePath) {
        // Default behavior, e.g., log the link
        log.info("File saved on path: {}", filePath);
    }

    @Override
    public void onFailure(String filePath, Exception e) {
        log.error("Failed to process data stream and store file: {}", filePath, e);
    }
}
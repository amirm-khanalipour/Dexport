package com.example.dexport.storage;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

/**
 * @author Amirmohammad Khanalipour
 */
public interface DexportFileStorage {
    Path getFilePath(String requestId, String extension);
    OutputStream getOutputStream(Path filePath) throws IOException;
}

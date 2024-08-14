package com.example.dexport.writer;

import java.io.OutputStream;
import java.util.stream.Stream;

/**
 * @author Amirmohammad Khanalipour
 */
public interface DexportItemWriter<T> {
    byte[] writeToByteArray(Stream<T> data);
    void writeToOutputStream(Stream<T> data, OutputStream outputStream);
    String getFormat();
}

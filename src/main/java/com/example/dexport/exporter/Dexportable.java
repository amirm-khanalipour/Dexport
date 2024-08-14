package com.example.dexport.exporter;

import com.example.dexport.config.DexportWriterConfig;
import com.example.dexport.mapper.DexportItemMapper;

import java.util.stream.Stream;

/**
 * @author Amirmohammad Khanalipour
 */
public interface Dexportable {
    <T> byte[] create(Stream<T> data, DexportItemMapper<T> dexportItemMapper, String format);
    <T> byte[] create(Stream<T> data, DexportItemMapper<T> dexportItemMapper, DexportWriterConfig dexportWriterConfig);
    <T> void createAsync(Stream<T> data, DexportItemMapper<T> dexportItemMapper, String format);
    <T> void createAsync(Stream<T> data, DexportItemMapper<T> dexportItemMapper, DexportWriterConfig dexportWriterConfig);
}

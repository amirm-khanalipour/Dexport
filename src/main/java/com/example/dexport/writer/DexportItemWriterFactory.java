package com.example.dexport.writer;

import com.example.dexport.config.DexportWriterConfig;
import com.example.dexport.mapper.DexportItemMapper;

import java.util.Map;

/**
 * @author Amirmohammad Khanalipour
 */
public class DexportItemWriterFactory {

    // Factory method for writers with default config
    public static <T> DexportItemWriter<T> getWriter(DexportItemMapper<T> dexportItemMapper, String format) {
        return switch (format.toLowerCase()) {
            case "csv" -> new DexportCsvItemWriter<>(dexportItemMapper);
            case "excel" -> new DexportExcelItemWriter<>(dexportItemMapper);
            case "pdf" -> new DexportPdfItemWriter.Builder<>(dexportItemMapper).build();
            default -> throw new UnsupportedOperationException("Unsupported file extension: " + format);
        };
    }

    // Factory method for configurable writers
    public static <T> DexportItemWriter<T> getWriter(DexportItemMapper<T> dexportItemMapper, DexportWriterConfig dexportWriterConfig) {
        return switch (dexportWriterConfig.getFormat().toLowerCase()) {
            case "pdf" -> createPdfItemWriter(dexportItemMapper, dexportWriterConfig);
            default -> throw new UnsupportedOperationException("Unsupported file extension: " + dexportWriterConfig.getFormat());
        };
    }

    private static <T> DexportPdfItemWriter<T> createPdfItemWriter(DexportItemMapper<T> dexportItemMapper, DexportWriterConfig dexportWriterConfig) {
        DexportPdfItemWriter.Builder<T> builder = new DexportPdfItemWriter.Builder<>(dexportItemMapper);
        Map<String, Object> configParams = dexportWriterConfig.getConfigParams();
        applyConfigIfPresent(configParams, builder);
        return builder.build();
    }

    private static <T> void applyConfigIfPresent(Map<String, Object> configParams, DexportPdfItemWriter.Builder<T> builder) {
        if (configParams.containsKey("title")) {
            Object title = configParams.get("title");
            if (title instanceof String) {
                builder.title((String) title);
            }
        }

        //More fields if support is added..
    }
}

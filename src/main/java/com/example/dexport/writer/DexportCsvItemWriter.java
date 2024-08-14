package com.example.dexport.writer;

import com.example.dexport.aop.DexportFileProcessingException;
import com.example.dexport.mapper.DexportItemMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.stream.Stream;

/**
 * @author Amirmohammad Khanalipour
 */
public class DexportCsvItemWriter<T> implements DexportItemWriter<T> {
    private static final Logger log = LoggerFactory.getLogger(DexportCsvItemWriter.class);
    private final DexportItemMapper<T> dexportItemMapper;

    public DexportCsvItemWriter(DexportItemMapper<T> dexportItemMapper) {
        this.dexportItemMapper = dexportItemMapper;
    }

    // Common method for writing CSV data
    private void writeCsv(Stream<T> data, OutputStream outputStream) {
        try (BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
             CSVPrinter csvPrinter = new CSVPrinter(fileWriter,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withAllowMissingColumnNames(true).withIgnoreHeaderCase())) {

            csvPrinter.printRecord(dexportItemMapper.mapHeaders());

            data.forEachOrdered(item -> {
                try {
                    csvPrinter.printRecord(dexportItemMapper.mapValues(item));
                } catch (IOException e) {
                    throw new DexportFileProcessingException("Failed to write record: " + e.getMessage(), e);
                }
            });

        } catch (IOException e) {
            throw new DexportFileProcessingException("Failed to write CSV file", e);
        }
    }

    @Override
    public byte[] writeToByteArray(Stream<T> data) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            writeCsv(data, outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new DexportFileProcessingException("Failed to write CSV file to byte array", e);
        }
    }

    @Override
    public void writeToOutputStream(Stream<T> data, OutputStream outputStream) {
        writeCsv(data, outputStream);
    }

    @Override
    public String getFormat() {
        return "csv";
    }
}


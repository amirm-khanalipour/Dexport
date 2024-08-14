package com.example.dexport.writer;

import com.example.dexport.mapper.DexportItemMapper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * @author Amirmohammad Khanalipour
 */
public class DexportExcelItemWriter<T> implements DexportItemWriter<T> {
    private static final Logger log = LoggerFactory.getLogger(DexportExcelItemWriter.class);
    private final DexportItemMapper<T> dexportItemMapper;

    public DexportExcelItemWriter(DexportItemMapper<T> dexportItemMapper) {
        this.dexportItemMapper = dexportItemMapper;
    }

    private void writeExcel(Stream<T> data, OutputStream outputStream) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet();
            Row headerRow = sheet.createRow(0);

            for (int index = 0; index < dexportItemMapper.mapHeaders().size(); index++) {
                headerRow.createCell(index).setCellValue(dexportItemMapper.mapHeaders().get(index));
            }

            AtomicInteger rowNum = new AtomicInteger(1);
            data.forEachOrdered(item -> {
                List<String> values = dexportItemMapper.mapValues(item);
                Row row = sheet.createRow(rowNum.getAndIncrement());
                for (int index = 0; index < values.size(); index++) {
                    row.createCell(index).setCellValue(values.get(index));
                }
            });

            workbook.write(outputStream);
        }
    }

    @Override
    public byte[] writeToByteArray(Stream<T> data) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            writeExcel(data, outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to write Excel file", e);
        }
    }

    @Override
    public void writeToOutputStream(Stream<T> data, OutputStream outputStream) {
        try {
            writeExcel(data, outputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write Excel file", e);
        }
    }

    @Override
    public String getFormat() {
        return "excel";
    }
}

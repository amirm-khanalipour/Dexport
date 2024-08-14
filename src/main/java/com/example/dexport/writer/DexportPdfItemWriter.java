package com.example.dexport.writer;

import com.example.dexport.aop.DexportFileProcessingException;
import com.example.dexport.config.DexportFontConfig;
import com.example.dexport.mapper.DexportItemMapper;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.font.FontProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.*;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author Amirmohammad Khanalipour
 */

public class DexportPdfItemWriter<T> implements DexportItemWriter<T> {
    private static final Logger log = LoggerFactory.getLogger(DexportPdfItemWriter.class);
    private final DexportItemMapper<T> dexportItemMapper;
    private final String title;
    private final int chunkSize;

    private DexportPdfItemWriter(Builder<T> builder) {
        this.dexportItemMapper = builder.dexportItemMapper;
        this.title = builder.title;
        this.chunkSize = builder.chunkSize;
    }

    private void writePdfInChunks(Iterator<T> dataIterator, OutputStream outputStream) throws IOException {
        try {
            while (dataIterator.hasNext()) {

            }
        } catch (Exception e) {
            throw new DexportFileProcessingException("Failed to write PDF file", e);
        }
    }

    private void convertHtmlToPdf(String html, OutputStream outputStream) {
        // Create a PdfWriter
        PdfWriter writer = new PdfWriter(outputStream);

        // Create a PdfDocument
        PdfDocument pdfDoc = new PdfDocument(writer);

        // Create a ConverterProperties object
        ConverterProperties properties = new ConverterProperties();

//        // Set up the custom FontProvider
        FontProvider fontProvider = new DexportFontConfig();
        properties.setFontProvider(fontProvider);

        // Convert HTML to PDF
        HtmlConverter.convertToPdf(html, pdfDoc, properties);
    }

    @Override
    public byte[] writeToByteArray(Stream<T> data) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            writePdfInChunks(data.iterator(), outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new DexportFileProcessingException("Failed to write PDF file", e);
        }
    }

    @Override
    public void writeToOutputStream(Stream<T> data, OutputStream outputStream) {
        try {
            writePdfInChunks(data.iterator(), outputStream);
        } catch (IOException e) {
            throw new DexportFileProcessingException("Failed to write PDF file", e);
        }
    }

    @Override
    public String getFormat() {
        return "pdf";
    }

    public static class Builder<T> {
        private final DexportItemMapper<T> dexportItemMapper;
        private String title = "title"; // Default
        private int chunkSize = 100; // Default

        public Builder(DexportItemMapper<T> dexportItemMapper) {
            this.dexportItemMapper = dexportItemMapper;
        }

        public Builder<T> title(String title) {
            this.title = title;
            return this;
        }

        public Builder<T> chunkSize(int chunkSize) {
            this.chunkSize = chunkSize;
            return this;
        }

        public DexportPdfItemWriter<T> build() {
            return new DexportPdfItemWriter<>(this);
        }
    }
}



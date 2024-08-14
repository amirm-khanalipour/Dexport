package com.example.dexport.config;

import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.layout.font.FontProvider;

import java.io.IOException;

public class DexportFontConfig extends FontProvider {
    public DexportFontConfig() {
        super();
        try {
            // Font style
            addCustomFont("fonts/ttf/Cardo-Bold.ttf", "cardo");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load custom fonts", e);
        }
    }

    private void addCustomFont(String fontPath, String alias) throws IOException {
        FontProgram fontProgram = FontProgramFactory.createFont(fontPath);
        this.getFontSet().addFont(fontProgram, getDefaultEncoding(fontProgram), alias);
    }
}
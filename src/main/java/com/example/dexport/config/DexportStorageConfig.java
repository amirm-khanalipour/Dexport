package com.example.dexport.config;

/**
 * @author Amirmohammad Khanalipour
 */
public class DexportStorageConfig {
    // Default to root project directory's "files" folder
    private String savingDir = "files";

    public DexportStorageConfig() {}
    public DexportStorageConfig(String savingDir) {
        this.savingDir = savingDir;
    }

    public String getSavingDir() {
        return savingDir;
    }

    public void setSavingDir(String savingDir) {
        this.savingDir = savingDir;
    }
}
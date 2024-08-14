package com.example.dexport.config;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Amirmohammad Khanalipour
 */
public class DexportWriterConfig {
    private String format;
    private Map<String, Object> configParams;

    public DexportWriterConfig(String format) {
        this.format = format;
        this.configParams = new HashMap<>();
    }

    public void addParam(String key, Object value) {
        configParams.put(key, value);
    }

    public String getFormat() {
        return format;
    }

    public Object getParam(String key) {
        return configParams.get(key);
    }

    public Map<String, Object> getConfigParams() {
        return configParams;
    }
}

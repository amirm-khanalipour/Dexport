package com.example.dexport.listener;

/**
 * @author Amirmohammad Khanalipour
 */
public interface DexportListener {
    void onFinish(String filePath);
    void onFailure(String filePath, Exception e);
}

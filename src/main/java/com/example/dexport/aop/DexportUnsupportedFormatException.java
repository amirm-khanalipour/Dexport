package com.example.dexport.aop;

/**
 * @author Amirmohammad Khanalipour
 */
public class DexportUnsupportedFormatException extends RuntimeException{
    public DexportUnsupportedFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.example.dexport.mapper;

import java.util.List;

/**
 * @author Amirmohammad Khanalipour
 */
public interface DexportItemMapper<T> {
    List<String> mapHeaders();
    List<String> mapValues(T t);
}

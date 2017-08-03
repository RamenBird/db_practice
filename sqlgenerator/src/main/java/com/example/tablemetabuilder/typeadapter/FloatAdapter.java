package com.example.tablemetabuilder.typeadapter;

import com.example.language.StorageType;
import com.example.tablemetabuilder.TypeAdapter;

/**
 * Created by RamenBird on 2017/8/2.
 */

public class FloatAdapter implements TypeAdapter {
    private static FloatAdapter sInstance;

    public static FloatAdapter instance() {
        if (sInstance == null)
            sInstance = new FloatAdapter();

        return sInstance;
    }

    @Override
    public StorageType getRawStorageType() {
        return StorageType.REAL;
    }

    @Override
    public String getStorageType() {
        return "REAL";
    }

    @Override
    public String getFromRawTypeText(String expression) {
        return expression;
    }

    @Override
    public String getFromDbTypeText(String expression) {
        return expression;
    }

    @Override
    public String getReadFromCursorText(String cursor, String columnIndex) {
        return String.format("%s.getFloat(%s)", cursor, columnIndex);
    }
}

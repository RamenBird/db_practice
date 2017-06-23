package com.example.tablemetabuilder.typeadapter;

import com.example.language.StorageType;
import com.example.tablemetabuilder.TypeAdapter;

/**
 * Created by RamenBird on 2017/6/23.
 */

public class IntAdapter implements TypeAdapter {
    private static IntAdapter sInstance;

    public static IntAdapter instance() {
        if (sInstance == null)
            sInstance = new IntAdapter();

        return sInstance;
    }

    @Override
    public StorageType getRawStorageType() {
        return StorageType.INTEGER;
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
        return String.format("%s.getInt(%s)", cursor, columnIndex);
    }
}

package com.example.tablemetabuilder.typeadapter;

import com.example.language.StorageType;
import com.example.tablemetabuilder.TypeAdapter;

/**
 * Created by RamenBird on 2017/7/21.
 */

public class StringAdapter implements TypeAdapter {
    private static StringAdapter sInstance;

    public static StringAdapter instance() {
        if (sInstance == null)
            sInstance = new StringAdapter();

        return sInstance;
    }

    @Override
    public StorageType getRawStorageType() {
        return StorageType.TEXT;
    }

    @Override
    public String getStorageType() {
        return "TEXT";
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
        return String.format("%s.getString(%s)", cursor, columnIndex);
    }
}
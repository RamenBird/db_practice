package com.example.tablemetabuilder.typeadapter;

import com.example.language.StorageType;
import com.example.tablemetabuilder.TypeAdapter;

/**
 * Created by RamenBird on 2017/6/23.
 */

public class TimeStampAdapter implements TypeAdapter {
    private static TimeStampAdapter instance;

    public static TimeStampAdapter instance() {
        if (instance == null)
            instance = new TimeStampAdapter();

        return instance;
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
        return expression + ".getTime()";
    }

    @Override
    public String getFromDbTypeText(String expression) {
        return "new Date(" + expression + ")";
    }

    @Override
    public String getReadFromCursorText(String cursor, String columnIndex) {
        return String.format("%s.getLong(%s)", cursor, columnIndex);
    }
}

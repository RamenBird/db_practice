package com.example.tablemetabuilder.typeadapter;

import com.example.language.StorageType;
import com.example.tablemetabuilder.TypeAdapter;

/**
 * Created by RamenBird on 2017/6/8.
 */

public class BooleanAdapter implements TypeAdapter {
    @Override
    public StorageType getRawStorageType() {
        return StorageType.INTEGER;
    }

    @Override
    public String getStorageType() {
        return "INTEGER";
    }

    @Override
    public String getFromRawTypeText(String expression) {
        return String.format("%s ? 1 : 0", expression);
    }

    @Override
    public String getFromDbTypeText(String expression) {
        return String.format("%s != 0", expression);
    }

    @Override
    public String getReadFromCursorText(String cursor, String columnIndex) {
        return String.format("%s.getInt(%s)", cursor, columnIndex);
    }
}

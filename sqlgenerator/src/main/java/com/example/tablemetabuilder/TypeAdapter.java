package com.example.tablemetabuilder;

import com.example.language.StorageType;

/**
 * Created by RamenBird on 2017/6/1.
 */

public interface TypeAdapter {
    StorageType getRawStorageType();

    String getStorageType();

    String getFromRawTypeText(String expression);

    String getFromDbTypeText(String expression);

    String getReadFromCursorText(String cursor, String columnIndex);
}

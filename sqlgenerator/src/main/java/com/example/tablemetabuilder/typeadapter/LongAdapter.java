package com.example.tablemetabuilder.typeadapter;

/**
 * Created by RamenBird on 2017/8/4.
 */

public class LongAdapter extends BaseTypeAdapter {
    private static LongAdapter sInstance;

    public static LongAdapter instance() {
        if (sInstance == null)
            sInstance = new LongAdapter();

        return sInstance;
    }

    @Override
    public String getStorageType() {
        return "INTEGER";
    }

    @Override
    public String getDtoTypeName() {
        return "long";
    }

    @Override
    public String getReadingExpression(String cursorParam, String columnName) {
        return String.format("%s.getLong(%s.getColumnIndex(%s))", cursorParam,
               cursorParam, columnName);
    }

    @Override
    public String getWritingExpression(String valueExpression) {
        return valueExpression;
    }

    @Override
    protected boolean isPrimitiveType() {
        return true;
    }
}

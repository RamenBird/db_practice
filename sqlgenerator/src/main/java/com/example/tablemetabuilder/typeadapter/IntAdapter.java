package com.example.tablemetabuilder.typeadapter;

/**
 * Created by RamenBird on 2017/6/23.
 */

public class IntAdapter extends BaseTypeAdapter {
    private static IntAdapter sInstance;

    public static IntAdapter instance() {
        if (sInstance == null)
            sInstance = new IntAdapter();

        return sInstance;
    }

    @Override
    public String getStorageType() {
        return "INTEGER";
    }

    @Override
    public String getDtoTypeName() {
        return "int";
    }

    @Override
    public String getReadingExpression(String cursorParam, String columnName) {
        return String.format("%s.getInt(%s.getColumnIndex(%s))", cursorParam,
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

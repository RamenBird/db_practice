package com.example.tablemetabuilder.typeadapter;

/**
 * Created by RamenBird on 2017/8/4.
 */

public class FloatAdapter extends BaseTypeAdapter {
    private static FloatAdapter sInstance;

    public static FloatAdapter instance() {
        if (sInstance == null)
            sInstance = new FloatAdapter();

        return sInstance;
    }

    @Override
    public String getStorageType() {
        return "REAL";
    }

    @Override
    public String getDtoTypeName() {
        return "float";
    }

    @Override
    public String getReadingExpression(String cursorParam, String columnName) {
        return String.format("%s.getFloat(%s.getColumnIndex(%s))", cursorParam,
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

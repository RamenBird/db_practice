package com.example.tablemetabuilder.typeadapter;

/**
 * Created by RamenBird on 2017/6/8.
 */

public class BooleanAdapter extends BaseTypeAdapter {
    private static BooleanAdapter sInstance;

    public static BooleanAdapter instance() {
        if (sInstance == null)
            sInstance = new BooleanAdapter();

        return sInstance;
    }

    @Override
    public String getStorageType() {
        return "INT2";
    }

    @Override
    public String getDtoTypeName() {
        return "bool";
    }

    @Override
    public String getReadingExpression(String cursorParam, String columnName) {
        return String.format("%s.getInt(%s.getColumnIndex(%s)) == 1? true : false", cursorParam,
               cursorParam, columnName);
    }

    @Override
    public String getWritingExpression(String valueExpression) {
        return valueExpression + " ? 1 : 0";
    }

    @Override
    protected boolean isPrimitiveType() {
        return true;
    }
}

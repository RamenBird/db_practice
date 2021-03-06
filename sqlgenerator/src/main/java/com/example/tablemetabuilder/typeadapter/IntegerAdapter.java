package com.example.tablemetabuilder.typeadapter;

/**
 * Created by RamenBird on 2017/6/23.
 */

public class IntegerAdapter extends BaseTypeAdapter {
    private static IntegerAdapter sInstance;

    public static IntegerAdapter instance() {
        if (sInstance == null)
            sInstance = new IntegerAdapter();

        return sInstance;
    }

    @Override
    public String getStorageType() {
        return "INTEGER";
    }

    @Override
    public String getDtoTypeName() {
        return "java.lang.Integer";
    }

    @Override
    public String getReadingFunctionContent(String cursorParam, String columnName) {
        String s = FUNCTION_TEMPLATE.replace("{1}", cursorParam).replace("{2}", columnName)
               .replace("{3}", "null").replace("{4}", "Int");

        return s;
    }

    @Override
    public String getWritingExpression(String valueExpression) {
        return valueExpression;
    }

    @Override
    protected boolean isPrimitiveType() {
        return false;
    }
}

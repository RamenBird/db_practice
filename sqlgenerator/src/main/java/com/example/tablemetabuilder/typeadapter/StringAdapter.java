package com.example.tablemetabuilder.typeadapter;

/**
 * Created by RamenBird on 2017/7/21.
 */

public class StringAdapter extends BaseTypeAdapter {
    private static StringAdapter sInstance;

    public static StringAdapter instance() {
        if (sInstance == null)
            sInstance = new StringAdapter();

        return sInstance;
    }

    @Override
    public String getStorageType() {
        return "TEXT";
    }

    @Override
    public String getDtoTypeName() {
        return "java.lang.String";
    }

    @Override
    public String getReadingFunctionContent(String cursorParam, String columnName) {
        String s = FUNCTION_TEMPLATE.replace("{1}", cursorParam).replace("{2}", columnName)
               .replace("{3}", "null").replace("{4}", "String");

        return s;
    }

    @Override
    public String getWritingExpression(String e) {
        return e;
    }

    @Override
    protected boolean isPrimitiveType() {
        return false;
    }
}
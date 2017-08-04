package com.example.tablemetabuilder.typeadapter;

/**
 * Created by RamenBird on 2017/8/4.
 */

public class LongRAdapter extends BaseTypeAdapter {
    private static LongRAdapter sInstance;

    public static LongRAdapter instance() {
        if (sInstance == null)
            sInstance = new LongRAdapter();

        return sInstance;
    }

    @Override
    public String getStorageType() {
        return "REAL";
    }

    @Override
    public String getDtoTypeName() {
        return "java.lang.Long";
    }

    @Override
    public String getReadingFunctionContent(String cursorParam, String columnName) {
        return FUNCTION_TEMPLATE.replace("{1}", cursorParam).replace("{2}", columnName)
               .replace("{3}", "null").replace("{4}", "Long");
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

package com.example.tablemetabuilder.typeadapter;

/**
 * Created by RamenBird on 2017/8/2.
 */

public class FloatRAdapter extends BaseTypeAdapter {
    private static FloatRAdapter sInstance;

    public static FloatRAdapter instance() {
        if (sInstance == null)
            sInstance = new FloatRAdapter();

        return sInstance;
    }

    @Override
    public String getStorageType() {
        return "REAL";
    }

    @Override
    public String getDtoTypeName() {
        return "java.lang.Float";
    }

    @Override
    public String getReadingFunctionContent(String cursorParam, String columnName) {
        return FUNCTION_TEMPLATE.replace("{1}", cursorParam).replace("{2}", columnName)
               .replace("{3}", "null").replace("{4}", "Float");
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

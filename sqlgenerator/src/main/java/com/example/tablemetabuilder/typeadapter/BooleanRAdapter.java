package com.example.tablemetabuilder.typeadapter;

/**
 * Created by RamenBird on 2017/8/4.
 */

public class BooleanRAdapter extends BaseTypeAdapter {
    private static BooleanRAdapter sInstance;

    public static BooleanRAdapter instance() {
        if (sInstance == null)
            sInstance = new BooleanRAdapter();

        return sInstance;
    }

    @Override
    public String getStorageType() {
        return "INT2";
    }

    @Override
    public String getDtoTypeName() {
        return "java.lang.Boolean";
    }

    @Override
    public String getReadingFunctionContent(String cursorParam, String columnName) {
        return FUNCTION_TEMPLATE.replace("{1}", cursorParam).replace("{2}", columnName)
               .replace("{3}", "Boolean.FALSE").replace("{4}", "Int").replace(");", ") == 1;");
    }

    @Override
    public String getWritingExpression(String valueExpression) {
        return String.format("%s != null && %s ? 1 : 0", valueExpression, valueExpression);
    }

    @Override
    protected boolean isPrimitiveType() {
        return false;
    }
}

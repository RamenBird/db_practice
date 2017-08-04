package com.example.tablemetabuilder.typeadapter;

/**
 * Created by RamenBird on 2017/6/23.
 */

public class TimeStampAdapter extends BaseTypeAdapter {
    private static TimeStampAdapter instance;

    public static TimeStampAdapter instance() {
        if (instance == null)
            instance = new TimeStampAdapter();

        return instance;
    }

    @Override
    public String getStorageType() {
        return "INTEGER";
    }

    @Override
    public String getDtoTypeName() {
        return "java.util.Date";
    }

    @Override
    public String getReadingFunctionContent(String cursorParam, String columnName) {
        return FUNCTION_TEMPLATE.replace("{1}", "new Date(" + cursorParam).replace("{2}", columnName)
               .replace("{3}", "null").replace("{4}", "Long").replace(");", "));");
    }

    @Override
    public String getWritingExpression(String e) {
        return String.format("%s.getTime()", e);
    }

    @Override
    protected boolean isPrimitiveType() {
        return false;
    }
}

package com.example.tablemetabuilder.typeadapter;

import com.example.tablemetabuilder.CursorAdapter;

/**
 * Created by RamenBird on 2017/8/3.
 */

public abstract class BaseTypeAdapter implements CursorAdapter {
    protected final static String INDENT = "    ";
    protected static String FUNCTION_TEMPLATE;

    public BaseTypeAdapter() {
        if (null == FUNCTION_TEMPLATE) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("try {\n");
            stringBuilder.append(INDENT);
            stringBuilder.append("return {1}.get{4}(cursor.getColumnIndex({2}));\n");
            stringBuilder.append("} catch (Exception e) {\n");
            stringBuilder.append(INDENT);
            stringBuilder.append("return {3};\n");
            stringBuilder.append("}\n");
            FUNCTION_TEMPLATE = stringBuilder.toString();
        }
    }


    @Override
    public final boolean useExpressionInReading() {
        return isPrimitiveType();
    }

    @Override
    public boolean useExpressionInWriting() {
        return true;
    }

    @Override
    public String getReadingExpression(String cursorParam, String columnName) {
        return null;
    }

    @Override
    public String getReadingFunctionContent(String cursorParam, String columnName) {
        return null;
    }

    @Override
    public String getWritingExpression(String valueExpression) {
        return null;
    }

    @Override
    public String getWritingFunctionContent(String valueExpression) {
        return null;
    }

    protected abstract boolean isPrimitiveType();
}

package com.example.tablemetabuilder;

/**
 * Created by RamenBird on 2017/6/1.
 */

public interface CursorAdapter {
    boolean useExpressionInReading();

    boolean useExpressionInWriting();

    String getStorageType();

    String getDtoTypeName();

    String getReadingExpression(String cursorParam, String columnName);

    String getReadingFunctionContent(String cursorParam, String columnName);

    String getWritingExpression(String valueExpression);

    String getWritingFunctionContent(String valueExpression);
}

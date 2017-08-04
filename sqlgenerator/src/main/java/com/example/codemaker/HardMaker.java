package com.example.codemaker;

import com.example.tablemeta.ColumnInfo;
import com.example.tablemeta.TableInfo;
import com.example.tablemetabuilder.Context;
import com.example.tablemetabuilder.ParseResult;

import java.util.List;

import javax.annotation.processing.Filer;

/**
 * Created by RamenBird on 2017/1/13.
 */

public class HardMaker implements SourceFileGenerator {
    private static final String ONE_INDENT = "    ";
    private static final String CONTINUATION = "       ";
    private static final String CHANGE_LINE_AND_ONE_INDENT = "\n    ";
    private static final String CHANGE_LINE_AND_ONE_INDENT_AND_CONTINUATION = "\n" + ONE_INDENT + CONTINUATION;
    private StringBuilder stringBuilder;

    protected static String generateBuildTableSql(Context context, List<TableInfo> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (TableInfo tableInfo : list) {
            if (null != tableInfo) {
                stringBuilder.append("create table ");
                stringBuilder.append(tableInfo.getTableName());
                stringBuilder.append("(");

                boolean flag = false;

                List<ColumnInfo> columnInfos = tableInfo.getColumns();
                for (int i = 0; i < columnInfos.size(); i++) {
                    ColumnInfo column = columnInfos.get(i);

                    stringBuilder.append(column.getColumnName());
                    stringBuilder.append(" ");
                    stringBuilder.append(context.getTypeAdapter(column).getStorageType());

                    if (!flag && column.isPrimaryKey()) {
                        stringBuilder.append(" PRIMARY KEY");
                        flag = true;
                    } else if (column.isPrimaryKey() || column.isUnique())
                        stringBuilder.append(" UNIQUE");

                    if (column.containNotNullConstraint())
                        stringBuilder.append(" NOT NULL");

                    if (column.isAutoIncrement())
                        stringBuilder.append(" AUTO_INCREMENT");

                    if (i != columnInfos.size() - 1)
                        stringBuilder.append(",");
                }
                stringBuilder.append(");");
            }
        }


        int cutSize = 80;
        int count = stringBuilder.length() / cutSize;
        String s = "\"+" + CHANGE_LINE_AND_ONE_INDENT_AND_CONTINUATION + "\"";

        int i = count;
        while (i > 0) {
            stringBuilder.insert(i * cutSize, s);
            i--;
        }

        stringBuilder.insert(0, CHANGE_LINE_AND_ONE_INDENT_AND_CONTINUATION + "\"");
        stringBuilder.append("\"");
        return stringBuilder.toString();
    }

    @Override
    public void generateSourceContent(Context context, ParseResult parseResult, Filer filer) {
//        try {
//            JavaFileObject javaFileObject = filer.createSourceFile(context.getTargetFileClassFullPath(), (Element) null);
//            Writer w = javaFileObject.openWriter();
//            List<String> imports = new ArrayList<>();
//
//            stringBuilder = new StringBuilder();
//            writePackageName(context.getTargetFileClassFullPath());
//
//            imports.add("android.database.sqlite.SQLiteDatabase");
//            imports.add("java.util.List");
//            imports.add("java.util.ArrayList");
//            imports.add("android.database.Cursor");
//            imports.add("android.content.ContentValues");
//
//            List<TableInfo> tableInfos = parseResult.getTableInfos();
//            for (TableInfo tableInfo : tableInfos) {
//                if (!context.useClassFullPath(tableInfo))
//                    imports.add(tableInfo.getClassRawInfo().getClassFullName());
//            }
//
//            writeImports(imports);
//
//            stringBuilder.append("public class ");
//
//            int index = context.getTargetFileClassFullPath().lastIndexOf(".");
//            if (index != -1)
//                stringBuilder.append(context.getTargetFileClassFullPath().substring(index + 1,
//                       context.getTargetFileClassFullPath().length()));
//            stringBuilder.append("{\n");
//
//            writeField("String", "CREATE_SQL_STATEMENT", generateBuildTableSql(context,
//                   tableInfos), 3, true, true);
//
//
//            FunctionContent content = new FunctionContent();
//
//            for (TableInfo table : tableInfos) {
//                if (table != null) {
//                    final boolean useFullClassFullPath = context.useClassFullPath(table);
//
//                    String dtoType = useFullClassFullPath ? table.getClassRawInfo().getClassFullName() :
//                           table.getClassRawInfo().getClassName();
//
//                    content.clear();
//                    content.addOneLineExpression("List<%s> list = new ArrayList<>();", dtoType);
//                    content.addOneLineExpression(String.format("Cursor cursor = p0.rawQuery(\"select * from %s\", null)",
//                           table.getTableName()));
//                    content.enterBlock(String.format("for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())"));
//                    content.addOneLineExpression("%s s = new %s()", dtoType, dtoType);
//
//                    for (ColumnInfo columnInfo : table.getColumns()) {
//                        CursorAdapter cursorAdapter = context.getTypeAdapter(columnInfo);
//                        if (cursorAdapter == null)
//                            continue;
//                        content.addOneLineExpression(String.format("s.%s(%s)",
//                               columnInfo.getFieldRawInfo().getSetterMethodName(),
//                               cursorAdapter.getFromDbTypeText(cursorAdapter.getReadFromCursorText("cursor",
//                                      "cursor.getColumnIndex(\"" + columnInfo.getColumnName() + "\")"))));
//                    }
//
//                    content.addOneLineExpression("list.add(s)");
//                    content.exitBlock();
//                    content.addOneLineExpression("cursor.close()");
//                    content.addOneLineExpression("return list", dtoType);
//                    writeFunction("getAll" + table.getTableName(), content, 1, true, true,
//                           String.format("List<%s>", dtoType), "SQLiteDatabase");
//
//                    content.clear();
//                    content.addOneLineExpression("ContentValues contentValues = new ContentValues()");
//                    for (ColumnInfo columnInfo : table.getColumns()) {
//                        content.addOneLineExpression("contentValues.put(\"%s\", {1}.%s())", columnInfo.getColumnName(),
//                               columnInfo.getFieldRawInfo().getGetterMethodName());
//                    }
//                    content.addOneLineExpression("{0}.insert(\"%s\", null, contentValues)", table.getTableName());
//
//                    writeFunction("add" + table.getTableName(), content, 1, true, true,
//                           "void", "SQLiteDatabase", dtoType);
//                }
//            }
//
//            content.clear();
//            content.enterBlock("if (CREATE_SQL_STATEMENT != \"\")", true);
//            content.addOneLineExpression("{0}.execSQL(CREATE_SQL_STATEMENT)");
//            content.exitBlock();
//            writeFunction("createTables", content, 1, true, true, "void", "SQLiteDatabase");
//
//            stringBuilder.append("}");
//            w.append(stringBuilder.toString());
//            w.flush();
//            w.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private void writePackageName(String fullPath) {
        int index = fullPath.lastIndexOf(".");
        if (index == -1)
            return;
        stringBuilder.append("package ");
        stringBuilder.append(fullPath.substring(0, index));
        stringBuilder.append(";\n");
    }

    private void writeImports(List<String> imports) {
        for (String s : imports) {
            stringBuilder.append("import ");
            stringBuilder.append(s);
            stringBuilder.append(";\n");
        }
    }

    private void writeField(String fieldType, String fieldName, String value, int accessControl, boolean isStatic, boolean isFinal) {
        stringBuilder.append(ONE_INDENT);

        if (accessControl == 1)
            stringBuilder.append("public ");
        else if (accessControl == 2)
            stringBuilder.append("protected ");
        else if (accessControl == 3)
            stringBuilder.append("private ");

        if (isStatic)
            stringBuilder.append("static ");

        if (isFinal)
            stringBuilder.append("final ");

        stringBuilder.append(String.format("%s %s = %s;\n", fieldType, fieldName, value));
    }

    private void writeFunction(String functionName, Object bodyTemplate, int accessControl,
                               boolean isStatic, boolean isFinal, String returnType, String... paramTypes) {
        stringBuilder.append(CHANGE_LINE_AND_ONE_INDENT);

        if (accessControl == 1)
            stringBuilder.append("public ");
        else if (accessControl == 2)
            stringBuilder.append("protected ");
        else if (accessControl == 3)
            stringBuilder.append("private ");

        if (isStatic)
            stringBuilder.append("static ");

        if (isFinal)
            stringBuilder.append("final ");

        stringBuilder.append(returnType);
        stringBuilder.append(" ");
        stringBuilder.append(functionName);
        stringBuilder.append("(");
        if (paramTypes.length > 0)
            for (int i = 0; i < paramTypes.length; i++) {
                stringBuilder.append(paramTypes[i]);
                stringBuilder.append(" ");
                stringBuilder.append("p" + String.valueOf(i));

                if (i != paramTypes.length - 1)
                    stringBuilder.append(", ");
            }


        stringBuilder.append("){\n");

        String body = bodyTemplate.toString();
        body = body.replaceAll("\\{(\\d)\\}", "p$1");

        stringBuilder.append(body);
        stringBuilder.append(ONE_INDENT);
        stringBuilder.append("}\n");
    }
}

package com.example.codemaker;

import com.example.annotation.Operation;
import com.example.annotation.Ramen;
import com.example.tablemeta.ColumnInfo;
import com.example.tablemeta.TableInfo;
import com.example.tablemetabuilder.Context;
import com.example.tablemetabuilder.CursorAdapter;
import com.example.tablemetabuilder.ParseResult;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

/**
 * Created by RamenBird on 2017/4/28.
 */

public class JavaPoetImpl extends HardMaker {
    private static final String FIELD_NAME_1 = "CREATE_TABLE_STATEMENT";
    private static final TypeName dbTypeName = ClassName.get("android.database.sqlite", "SQLiteDatabase");
    private static final TypeName cvTypeName = ClassName.get("android.content", "ContentValues");
    private static final TypeName cursorTypeName = ClassName.get("android.database", "Cursor");
    private HashMap<String, MethodSpec> mAccessorMethods = new HashMap<>();

    private static boolean needReaderMethod(TypeMirror typeMirror) {
        return typeMirror.getKind() == TypeKind.DECLARED;
    }

    private static boolean needWriterMethod(TypeMirror typeMirror) {
        return false;
    }

    private static ColumnInfo findUniqueAndStableColumn(List<ColumnInfo> columnInfos) {
        for (ColumnInfo columnInfo : columnInfos) {
            if ((columnInfo.isPrimaryKey() || columnInfo.isUnique()) && columnInfo.isStable())
                return columnInfo;
        }
        return null;
    }

    @Override
    public void generateSourceContent(Context context, ParseResult parseResult, Filer filer) {
        mAccessorMethods.clear();
        try {
            TypeSpec.Builder classBuilder = TypeSpec.classBuilder("GeneratedSqlClass")
                   .addModifiers(Modifier.PUBLIC);
            ClassName listClassName = ClassName.get(List.class);

            List<TableInfo> tableInfos = parseResult.getTableInfos();
            FieldSpec createTable = FieldSpec.builder(String.class, FIELD_NAME_1, Modifier.FINAL,
                   Modifier.STATIC, Modifier.PRIVATE).initializer(
                   generateBuildTableSql(context, tableInfos)).build();
            classBuilder.addField(createTable);

            MethodSpec createTableMethod = MethodSpec.methodBuilder("createTables")
                   .addModifiers(Modifier.FINAL, Modifier.STATIC, Modifier.PUBLIC)
                   .addParameter(dbTypeName, "p0")
                   .beginControlFlow("for (String s : $N.split(\";\"))", createTable)
                   .addStatement("p0.execSQL(s)")
                   .endControlFlow()
                   .build();
            classBuilder.addMethod(createTableMethod);

            for (TableInfo table : tableInfos) {
                TypeName paramType = ClassName.get(table.getClassRawInfo().getRawTypeElement().asType());
                MethodSpec.Builder addRecordMethodBuilder = MethodSpec.methodBuilder("add" + table.getTableName())
                       .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                       .addParameter(dbTypeName, "db")
                       .addParameter(paramType, "item")
                       .addStatement("$T contentValues = new $T()", cvTypeName, cvTypeName);

                Ramen ramen = table.getClassRawInfo().getRawTypeElement().getAnnotation(Ramen.class);
                if (ramen != null) {
                    if (Arrays.binarySearch(ramen.value(), Operation.find) != -1) {


                    }

                    if (Arrays.binarySearch(ramen.value(), Operation.update) != -1) {
                        ColumnInfo primaryColumn = findUniqueAndStableColumn(table.getColumns());
                        if (primaryColumn != null) { //update all
                            MethodSpec.Builder updateMethodBuilder = MethodSpec.methodBuilder("update" + table.getTableName())
                                   .returns(TypeName.VOID)
                                   .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                                   .addParameter(dbTypeName, "db")
                                   .addParameter(paramType, "item");

                            String whereArgs = String.format("new String[]{String.valueOf(item.%s())}", primaryColumn.getFieldRawInfo().getGetterMethodName());

                            updateMethodBuilder.addStatement("ContentValues contentValues = new ContentValues()");
                            for (ColumnInfo column : table.getColumns()) {
                                if (column.isStable())
                                    continue;
                                CursorAdapter adapter = context.getTypeAdapter(column);


                                updateMethodBuilder.addStatement(String.format("contentValues.put($S, %s)",
                                       getWritingStatement(adapter, "cursor", String.format("item.%s()", column.getFieldRawInfo().getGetterMethodName()))),
                                       column.getColumnName());
                            }
                            updateMethodBuilder.addStatement(String.format("db.update(\"%s\", contentValues, \"%s=?\", %s)",
                                   table.getTableName(), primaryColumn.getColumnName(), whereArgs));

                            classBuilder.addMethod(updateMethodBuilder.build());
                        }
                    }
                }


                TypeName getAllMethodReturnType = ParameterizedTypeName.get(listClassName, paramType);
                MethodSpec.Builder getAllMethodBuilder = MethodSpec.methodBuilder(String.format("getAll%ss",
                       table.getTableName()))
                       .returns(getAllMethodReturnType)
                       .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                       .addParameter(dbTypeName, "db")
                       .addStatement(String.format("$T cursor = db.rawQuery(\"select * from %s;\", null)",
                              table.getTableName()), cursorTypeName)
                       .addStatement("$T list = new $T()", getAllMethodReturnType, ParameterizedTypeName.get(ClassName.get(ArrayList.class),
                              paramType))
                       .beginControlFlow("for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())")
                       .addStatement("$T item = new $T()", paramType, paramType);

                List<ColumnInfo> columnInfos = table.getColumns();
                for (ColumnInfo columnInfo : columnInfos) {
                    CursorAdapter adapter = context.getTypeAdapter(columnInfo);

                    if (!(columnInfo.isPrimaryKey() && columnInfo.isAutoIncrement())) {
                        addRecordMethodBuilder.addStatement(
                               String.format("contentValues.put($S, %s)",
                                      getWritingStatement(adapter, "cursor", String.format("item.%s()", columnInfo.getFieldRawInfo().getGetterMethodName()))),
                               columnInfo.getColumnName());
                    }

                    TypeMirror typeMirror = columnInfo.getFieldRawInfo().getFieldElement().asType();
                    getAllMethodBuilder.addStatement(
                           String.format("item.%s(%s)", columnInfo.getFieldRawInfo().getSetterMethodName(),
                                  getReadingStatement(adapter, "cursor", columnInfo.getColumnName())));

                }


                addRecordMethodBuilder.addStatement("db.insert($S, null, contentValues)", table.getTableName());
                getAllMethodBuilder.addStatement("list.add(item)");
                getAllMethodBuilder.endControlFlow();
                getAllMethodBuilder.addStatement("return list");

                classBuilder.addMethod(addRecordMethodBuilder.build());
                classBuilder.addMethod(getAllMethodBuilder.build());
            }

            for (Map.Entry<String, MethodSpec> entry : mAccessorMethods.entrySet()) {
                classBuilder.addMethod(entry.getValue());
            }

            JavaFile javaFile = JavaFile.builder("com.ramenbird", classBuilder.build()).build();
            javaFile.writeTo(filer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final String getReadingStatement(CursorAdapter adapter, String cursorName, String columnName) {
        if (adapter.useExpressionInReading())
            return adapter.getReadingExpression(cursorName, "\"" + columnName + "\"");

        String funcKey = adapter.getDtoTypeName() + "reader";
        String methodName = "convertTo" + adapter.getClass().getSimpleName().toString();

        if (!mAccessorMethods.containsKey(funcKey)) {
            MethodSpec.Builder builder = MethodSpec.methodBuilder(methodName);
            builder.returns(ClassName.bestGuess(adapter.getDtoTypeName()));
            builder.addParameter(cursorTypeName, "cursor");
            builder.addParameter(ClassName.get(String.class), "columnName");
            builder.addCode(adapter.getReadingFunctionContent("cursor", "columnName"));
            builder.addModifiers(Modifier.PRIVATE, Modifier.STATIC);

            mAccessorMethods.put(funcKey, builder.build());
        }

        return String.format("%s(%s, \"%s\")", methodName, cursorName, columnName);
    }

    private final String getWritingStatement(CursorAdapter adapter, String cursorName, String valueExpression) {
        if (adapter.useExpressionInWriting())
            return adapter.getWritingExpression(valueExpression);

        String funcKey = adapter.getDtoTypeName() + "writer";
        String methodName = "convertWith" + adapter.getClass().getSimpleName().toString();

        if (!mAccessorMethods.containsKey(funcKey)) {
            MethodSpec.Builder builder = MethodSpec.methodBuilder(methodName);
            builder.returns(ClassName.bestGuess(adapter.getDtoTypeName()));
            builder.addParameter(ClassName.get(String.class), "columnName");
            builder.addCode(adapter.getWritingFunctionContent("columnName"));
            builder.addModifiers(Modifier.PRIVATE, Modifier.STATIC);

            mAccessorMethods.put(funcKey, builder.build());
        }

        return String.format("%s(%s, %s)", methodName, cursorName, valueExpression);
    }
}

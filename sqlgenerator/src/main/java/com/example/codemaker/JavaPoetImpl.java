package com.example.codemaker;

import com.example.tablemeta.ColumnInfo;
import com.example.tablemeta.TableInfo;
import com.example.tablemetabuilder.Context;
import com.example.tablemetabuilder.TypeAdapter;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
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
    private HashMap<String, MethodSpec> cursorReader = new HashMap<>();

    private static boolean needReaderMethod(TypeMirror typeMirror) {
        return typeMirror.getKind() == TypeKind.DECLARED;
    }

    @Override
    public void generateSourceContent(Context context, List<TableInfo> tableInfos, Filer filer) {
        cursorReader.clear();
        try {
            TypeSpec.Builder classBuilder = TypeSpec.classBuilder("GeneratedSqlClass")
                   .addModifiers(Modifier.PUBLIC);
            TypeName dbTypeName = ClassName.get("android.database.sqlite", "SQLiteDatabase");
            TypeName cvTypeName = ClassName.get("android.content", "ContentValues");
            TypeName cursorTypeName = ClassName.get("android.database", "Cursor");
            ClassName listClassName = ClassName.get(List.class);

            FieldSpec createTable = FieldSpec.builder(String.class, FIELD_NAME_1, Modifier.FINAL,
                   Modifier.STATIC, Modifier.PRIVATE).initializer(
                   generateBuildTableSql(context, tableInfos)).build();
            classBuilder.addField(createTable);

            MethodSpec createTableMethod = MethodSpec.methodBuilder("createTables")
                   .addModifiers(Modifier.FINAL, Modifier.STATIC, Modifier.PUBLIC)
                   .addParameter(dbTypeName, "p0")
                   .addStatement("p0.execSQL(" + FIELD_NAME_1 + ")")
                   .build();
            classBuilder.addMethod(createTableMethod);

            for (TableInfo info : tableInfos) {
                TypeName paramType = ClassName.get(info.getClassRawInfo().getRawTypeElement().asType());
                MethodSpec.Builder addRecordMethodBuilder = MethodSpec.methodBuilder("add" + info.getTableName())
                       .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                       .addParameter(dbTypeName, "db")
                       .addParameter(paramType, "item")
                       .addStatement("$T contentValues = new $T()", cvTypeName, cvTypeName);


                TypeName getAllMethodReturnType = ParameterizedTypeName.get(listClassName, paramType);
                MethodSpec.Builder getAllMethodBuilder = MethodSpec.methodBuilder(String.format("getAll%ss",
                       info.getTableName()))
                       .returns(getAllMethodReturnType)
                       .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                       .addParameter(dbTypeName, "db")
                       .addStatement(String.format("$T cursor = db.rawQuery(\"select * from %s;\", null)",
                              info.getTableName()), cursorTypeName)
                       .addStatement("$T list = new $T()", getAllMethodReturnType, ParameterizedTypeName.get(ClassName.get(ArrayList.class),
                              paramType))
                       .beginControlFlow("for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())")
                       .addStatement("$T item = new $T()", paramType, paramType);

                List<ColumnInfo> columnInfos = info.getColumns();
                for (ColumnInfo columnInfo : columnInfos) {
                    TypeAdapter adapter = context.getTypeAdapter(columnInfo);
                    addRecordMethodBuilder.addStatement(String.format("contentValues.put($S, %s)",
                           adapter.getFromRawTypeText("item." + columnInfo.getFieldRawInfo().getGetterMethodName() + "()")),
                           columnInfo.getColumnName());

                    TypeMirror typeMirror = columnInfo.getFieldRawInfo().getFieldElement().asType();
                    if (needReaderMethod(typeMirror)) {
                        MethodSpec methodSpec = cursorReader.containsKey(typeMirror.toString()) ?
                               cursorReader.get(typeMirror.toString()) :
                               createCursorReader(typeMirror, adapter);

                        getAllMethodBuilder.addStatement("item." + columnInfo.getFieldRawInfo().getSetterMethodName() +
                               "($N(cursor, cursor.getColumnIndex($S)))", methodSpec, columnInfo.getColumnName());
                    } else {
                        getAllMethodBuilder.addStatement("item." + columnInfo.getFieldRawInfo().getSetterMethodName() +
                                      "(" + adapter.getFromDbTypeText(adapter.getReadFromCursorText("cursor", "cursor.getColumnIndex($S)")) + ")",
                               columnInfo.getColumnName());
                    }
                }


                addRecordMethodBuilder.addStatement("db.insert($S, null, contentValues)", info.getTableName());
                getAllMethodBuilder.addStatement("list.add(item)");
                getAllMethodBuilder.endControlFlow();
                getAllMethodBuilder.addStatement("return list");

                classBuilder.addMethod(addRecordMethodBuilder.build());
                classBuilder.addMethod(getAllMethodBuilder.build());
            }

            for (Map.Entry<String, MethodSpec> entry : cursorReader.entrySet()) {
                classBuilder.addMethod(entry.getValue());
            }

            JavaFile javaFile = JavaFile.builder("com.ramenbird", classBuilder.build()).build();
            javaFile.writeTo(filer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MethodSpec createCursorReader(TypeMirror s, TypeAdapter adapter) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("convertWith" + adapter.getClass().getSimpleName().toString());
        builder.addModifiers(Modifier.STATIC);
        builder.addParameter(ClassName.get("android.database", "Cursor"), "cursor");
        builder.addParameter(int.class, "index");
        builder.returns(TypeName.get(s));
        builder.beginControlFlow("try");
        builder.addStatement(String.format("$T value = %s", adapter.getFromDbTypeText(adapter.getReadFromCursorText("cursor", "index"))), s);
        builder.addStatement("return value");
        builder.endControlFlow();
        builder.beginControlFlow("catch (Exception e)");
        builder.endControlFlow();
        builder.addStatement("return null", s);
        this.cursorReader.put(s.toString(), builder.build());
        return builder.build();
    }
}

package com.example.codemaker;

import com.example.tablemetabuilder.Context;
import com.example.tablemeta.ColumnInfo;
import com.example.tablemeta.TableInfo;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

/**
 * Created by RamenBird on 2017/4/28.
 */

public class JavaPoetImpl extends HardMaker {
    private static final String FIELD_NAME_1 = "CREATE_TABLE_STATEMENT";

    @Override
    public void generateSourceContent(Context context, List<TableInfo> tableInfos, Filer filer) {
        try {
            TypeSpec.Builder classBuilder = TypeSpec.classBuilder("GeneratedSqlClass")
                   .addModifiers(Modifier.PUBLIC);
            TypeName dbTypeName = ClassName.get("android.database.sqlite", "SQLiteDatabase");
            TypeName cvTypeName = ClassName.get("android.content", "ContentValues");
            TypeName cursorTypeName = ClassName.get("android.database", "Cursor");

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

                MethodSpec.Builder getAllMethodBuilder = MethodSpec.methodBuilder(String.format("getAll%ss",
                       info.getTableName()))
                       .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                       .addParameter(dbTypeName, "db")
                       .addStatement(String.format("$T cursor = db.rawQuery(\"select * from %s;\", null)",
                              info.getTableName()), cursorTypeName);

                List<ColumnInfo> columnInfos = info.getColumns();
                for (ColumnInfo columnInfo : columnInfos) {
                    addRecordMethodBuilder.addStatement("contentValues.put($S, item." + columnInfo.getFieldRawInfo()
                           .getGetterMethodName() + "())", columnInfo.getColumnName());
                }


                addRecordMethodBuilder.addStatement("db.insert($S, null, contentValues)", info.getTableName());

                classBuilder.addMethod(addRecordMethodBuilder.build());
                classBuilder.addMethod(getAllMethodBuilder.build());
            }

            JavaFile javaFile = JavaFile.builder("com.ramenbird", classBuilder.build()).build();
            javaFile.writeTo(filer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

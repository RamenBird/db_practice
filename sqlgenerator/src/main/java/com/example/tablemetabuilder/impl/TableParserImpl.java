package com.example.tablemetabuilder.impl;

import com.example.annotation.Column;
import com.example.tablemeta.TableInfo;
import com.example.tablemetabuilder.Context;
import com.example.tablemetabuilder.ParseResult;
import com.example.tablemetabuilder.TableParser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.tools.Diagnostic;

/**
 * Created by RamenBird on 2017/1/3.
 */

class TableParserImpl implements TableParser {
    private static final Pattern LOWER_CASE = Pattern.compile("^[a-z](([^A-Z].*$)|$)");
    private ProcessingEnvironment mProcessingEnvironment;

    public TableParserImpl(ProcessingEnvironment processingEnv) {
        this.mProcessingEnvironment = processingEnv;
    }

    private static final String doSetterNameTransfer(String fieldName) {
        if (isEmpty(fieldName))
            return null;

        if (LOWER_CASE.matcher(fieldName).matches())
            return "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        else
            return "set" + fieldName;
    }

    private static final String doGetterNameTransfer(String fieldName, boolean b) {
        if (isEmpty(fieldName))
            return null;

        String prefix = b ? "is" : "get";

        if (LOWER_CASE.matcher(fieldName).matches())
            return prefix + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        else
            return prefix + fieldName;
    }

    private final static boolean isEmpty(String s) {
        return s == null || s.equals("");
    }

    private static final void getFieldAccessorNames(String fieldName, Column columnInfo, String[] methodsNames, boolean b) {
        if (columnInfo == null) {
            methodsNames[0] = doGetterNameTransfer(fieldName, b);
            methodsNames[1] = doSetterNameTransfer(fieldName);
        } else {
            if (columnInfo.ignore())
                return;

            methodsNames[0] = isEmpty(columnInfo.getter()) ? doGetterNameTransfer(fieldName, b) :
                   columnInfo.getter();
            methodsNames[1] = isEmpty(columnInfo.setter()) ? doSetterNameTransfer(fieldName) :
                   columnInfo.setter();
        }
    }

    private static boolean isSqlKeyword(String s) {
        return "Transaction".equals(s);
    }

    private static String resolveClassName(String tableName, List<TableInfo> tableInfos) {
        if (isSqlKeyword(tableName)) {
            return resolveClassName(tableName + "1", tableInfos);
        }

        for (TableInfo tableInfo : tableInfos) {
            if (tableInfo.getTableName().equals(tableName)) {
                return resolveClassName(tableName + "1", tableInfos);
            }
        }

        return tableName;
    }

    @Override
    public ParseResult parseTableMeta(Context context, Collection<? extends Element> elements) {
        ParseResult parseResult = new ParseResult();
        List<TableInfo> tableInfos = new ArrayList<>();
        parseResult.setTableInfos(tableInfos);

        for (Element element : elements) {
            TableInfoImpl tableInfoImpl = new TableInfoImpl();

            Name name = mProcessingEnvironment.getElementUtils().getBinaryName((TypeElement) element);

            tableInfoImpl.mDbClass = new ClassRawInfoImpl(element,
                   element.getSimpleName().toString(),
                   name.toString());
            tableInfoImpl.tableName = resolveClassName(element.getSimpleName().toString(), tableInfos);
            List<String> methodNames = new ArrayList<>();
            List<Element> fields = new ArrayList<>();

            for (Element enclosedElement : element.getEnclosedElements()) {
                if (enclosedElement.getKind() == ElementKind.METHOD && enclosedElement instanceof ExecutableElement) {
                    ExecutableElement executableElement = (ExecutableElement) enclosedElement;
                    Set<Modifier> modifiers = executableElement.getModifiers();
                    if (modifiers.contains(Modifier.PUBLIC) && !modifiers.contains(Modifier.STATIC))
                        methodNames.add(executableElement.getSimpleName().toString());

                    continue;
                }

                if (enclosedElement.getKind() != ElementKind.FIELD)
                    continue;

                Set<Modifier> modifiers = enclosedElement.getModifiers();
                if (modifiers.contains(Modifier.STATIC))
                    continue;

                if (context.supportType(enclosedElement.asType())) {
                    fields.add(enclosedElement);
                }
            }

            //avoid multi-primary annotation
            boolean flag = true;
            for (Element fieldElement : fields) {
                Column rawInfo = fieldElement.getAnnotation(Column.class);
                String fieldName = fieldElement.getSimpleName().toString();
                String[] outMethodNames = new String[2];

                getFieldAccessorNames(fieldName, rawInfo, outMethodNames, fieldElement.asType().getKind() == TypeKind.BOOLEAN);

                if (!methodNames.contains(outMethodNames[0]) || !methodNames.contains(outMethodNames[1]))
                    continue;

                ColumnInfoImpl column = new ColumnInfoImpl();
                column.columnName = fieldName;
                column.fieldRawInfo = new FieldRawInfoImpl();
                column.fieldRawInfo.getterName = outMethodNames[0];
                column.fieldRawInfo.setterName = outMethodNames[1];
                column.fieldRawInfo.fieldName = fieldName;
                column.fieldRawInfo.rawElement = fieldElement;
                column.flags[0] = flag && rawInfo != null && rawInfo.primary();
                column.flags[1] = rawInfo != null && rawInfo.stable();
                column.flags[2] = rawInfo != null && rawInfo.autoIncrement();
                column.flags[3] = rawInfo != null && rawInfo.unique();
                column.flags[4] = rawInfo != null && rawInfo.notNull();

                if (column.flags[0])
                    flag = false;

                tableInfoImpl.columns.add(column);
            }


            tableInfos.add(tableInfoImpl);
        }

        return parseResult;
    }

    @Override
    public void changeTableName(TableInfo info, String newTableName) {
        TableInfoImpl meta = (TableInfoImpl) info;
        meta.tableName = newTableName;
    }

    private void print(String s) {
        mProcessingEnvironment.getMessager().printMessage(Diagnostic.Kind.NOTE, s);
    }
}

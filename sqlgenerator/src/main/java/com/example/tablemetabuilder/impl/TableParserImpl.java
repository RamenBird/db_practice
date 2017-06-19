package com.example.tablemetabuilder.impl;

import com.example.annotation.Column;
import com.example.tablemeta.TableInfo;
import com.example.tablemetabuilder.Context;
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

    private static final String doGetterNameTransfer(String fieldName) {
        if (isEmpty(fieldName))
            return null;

        if (LOWER_CASE.matcher(fieldName).matches())
            return "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        else
            return "get" + fieldName;
    }

    private final static boolean isEmpty(String s) {
        return s == null || s.equals("");
    }

    @Override
    public List<TableInfo> parseTableMeta(Context context, Collection<? extends Element> elements) {
        List<TableInfo> tableInfos = new ArrayList<>();

        for (Element element : elements) {
            TableInfoImpl tableInfoImpl = new TableInfoImpl();

            Name name = mProcessingEnvironment.getElementUtils().getBinaryName((TypeElement) element);

            tableInfoImpl.mDbClass = new ClassRawInfoImpl(element,
                   element.getSimpleName().toString(),
                   name.toString());
            tableInfoImpl.tableName = element.getSimpleName().toString();
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

                if (context.supportType(enclosedElement.asType()))
                    fields.add(enclosedElement);
            }

            for (Element element1 : fields) {
                Column columnInfo = element1.getAnnotation(Column.class);
                String fieldName = element1.getSimpleName().toString();
                if (columnInfo != null) {
                    if (!columnInfo.ignore()) {
                        final String s1 = isEmpty(columnInfo.getter()) ? doGetterNameTransfer(fieldName) :
                               columnInfo.getter();

                        if (!methodNames.contains(s1))
                            continue;

                        final String s2 = isEmpty(columnInfo.setter()) ? doSetterNameTransfer(fieldName) :
                               columnInfo.setter();

                        if (!methodNames.contains(s2))
                            continue;

                        ColumnInfoImpl column = new ColumnInfoImpl();
                        column.columnName = fieldName;
                        column.field = new FieldRawInfoImpl();
                        column.field.getterName = s1;
                        column.field.setterName = s2;
                        column.field.fieldName = fieldName;

                        tableInfoImpl.columns.add(column);
                    }
                } else {
                    final String s1 = doGetterNameTransfer(fieldName);
                    if (!methodNames.contains(s1))
                        continue;

                    final String s2 = doSetterNameTransfer(fieldName);
                    if (!methodNames.contains(s2))
                        continue;

                    ColumnInfoImpl column = new ColumnInfoImpl();
                    column.columnName = fieldName;
                    column.field = new FieldRawInfoImpl();
                    column.field.getterName = s1;
                    column.field.setterName = s2;
                    column.field.fieldName = fieldName;

                    tableInfoImpl.columns.add(column);
                }
            }

            tableInfos.add(tableInfoImpl);
        }

        return tableInfos;
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

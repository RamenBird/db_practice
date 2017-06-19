package com.example.tablemetabuilder;

import com.example.tablemeta.ColumnInfo;
import com.example.tablemeta.TableInfo;

import javax.lang.model.type.TypeMirror;

/**
 * Created by RamenBird on 2017/1/16.
 */

public abstract class Context {
    public abstract boolean supportForeignKey();

    public abstract boolean supportType(TypeMirror typeMirror);

    public abstract TypeAdapter getTypeAdapter(ColumnInfo columnInfo);

    public abstract String getTargetFileClassFullPath();

    public boolean useClassFullPath(TableInfo tableInfo) {
        return !tableInfo.getClassRawInfo().getClassName().equals(tableInfo.getTableName());
    }
}

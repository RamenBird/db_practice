package com.example;

import com.example.tablemeta.ColumnInfo;
import com.example.tablemetabuilder.Context;
import com.example.tablemetabuilder.TypeAdapter;

import javax.lang.model.type.TypeMirror;

/**
 * Created by RamenBird on 2017/6/8.
 */

public class CurrentContext extends Context {
    @Override
    public boolean supportForeignKey() {
        return false;
    }

    @Override
    public boolean supportType(TypeMirror typeMirror) {
        return false;
    }

    @Override
    public TypeAdapter getTypeAdapter(ColumnInfo columnInfo) {
        return null;
    }

    @Override
    public String getTargetFileClassFullPath() {
        return "com.ramenbird.GeneratedSqlClass";
    }
}

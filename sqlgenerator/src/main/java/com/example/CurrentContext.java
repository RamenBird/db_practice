package com.example;

import com.example.tablemeta.ColumnInfo;
import com.example.tablemetabuilder.Context;
import com.example.tablemetabuilder.TypeAdapter;
import com.example.tablemetabuilder.typeadapter.BooleanAdapter;
import com.example.tablemetabuilder.typeadapter.IntAdapter;
import com.example.tablemetabuilder.typeadapter.IntegerAdapter;
import com.example.tablemetabuilder.typeadapter.TimeStampAdapter;

import java.util.Date;

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
        return getTypeAdapter(typeMirror) != null;
    }

    @Override
    public TypeAdapter getTypeAdapter(ColumnInfo columnInfo) {
        return getTypeAdapter(columnInfo.getFieldRawInfo().getFieldElement().asType());
    }

    @Override
    public String getTargetFileClassFullPath() {
        return "com.ramenbird.GeneratedSqlClass";
    }

    private TypeAdapter getTypeAdapter(TypeMirror typeMirror) {
        switch (typeMirror.getKind()) {
            case BOOLEAN:
                return BooleanAdapter.instance();
            case INT:
                return IntAdapter.instance();
            case DECLARED:
                if (typeMirror.toString().equals("java.lang.Integer"))
                    return IntegerAdapter.instance();
                if (typeMirror.toString().equals("java.util.Date"))
                    return TimeStampAdapter.instance();
        }
        return null;
    }
}

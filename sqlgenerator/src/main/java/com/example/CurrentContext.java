package com.example;

import com.example.tablemeta.ColumnInfo;
import com.example.tablemetabuilder.Context;
import com.example.tablemetabuilder.CursorAdapter;
import com.example.tablemetabuilder.typeadapter.BooleanAdapter;
import com.example.tablemetabuilder.typeadapter.BooleanRAdapter;
import com.example.tablemetabuilder.typeadapter.FloatAdapter;
import com.example.tablemetabuilder.typeadapter.FloatRAdapter;
import com.example.tablemetabuilder.typeadapter.IntAdapter;
import com.example.tablemetabuilder.typeadapter.IntegerAdapter;
import com.example.tablemetabuilder.typeadapter.LongAdapter;
import com.example.tablemetabuilder.typeadapter.LongRAdapter;
import com.example.tablemetabuilder.typeadapter.StringAdapter;
import com.example.tablemetabuilder.typeadapter.TimeStampAdapter;

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
    public CursorAdapter getTypeAdapter(ColumnInfo columnInfo) {
        return getTypeAdapter(columnInfo.getFieldRawInfo().getFieldElement().asType());
    }

    @Override
    public String getTargetFileClassFullPath() {
        return "com.ramenbird.GeneratedSqlClass";
    }

    private CursorAdapter getTypeAdapter(TypeMirror typeMirror) {
        switch (typeMirror.getKind()) {
            case BOOLEAN:
                return BooleanAdapter.instance();
            case INT:
                return IntAdapter.instance();
            case LONG:
                return LongAdapter.instance();
            case FLOAT:
                return FloatAdapter.instance();
            case DECLARED:
                if (typeMirror.toString().equals("java.lang.Boolean"))
                    return BooleanRAdapter.instance();
                if (typeMirror.toString().equals("java.lang.Integer"))
                    return IntegerAdapter.instance();
                if (typeMirror.toString().equals("java.util.Date"))
                    return TimeStampAdapter.instance();
                if (typeMirror.toString().equals("java.lang.String"))
                    return StringAdapter.instance();
                if (typeMirror.toString().equals("java.lang.Float"))
                    return FloatRAdapter.instance();
                if (typeMirror.toString().equals("java.lang.Long"))
                    return LongRAdapter.instance();
        }
        return null;
    }
}

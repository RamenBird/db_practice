package com.example.tablemetabuilder.impl;

import com.example.tablemeta.ColumnInfo;
import com.example.tablemeta.FieldRawInfo;
import com.example.tablemeta.ForeignRefer;

/**
 * Created by RamenBird on 2017/5/19.
 */

class ColumnInfoImpl implements ColumnInfo, ForeignRefer {
    String columnName;
    FieldRawInfoImpl fieldRawInfo;
    boolean[] flags = new boolean[5];

    @Override
    public String getColumnName() {
        return columnName;
    }

    @Override
    public ForeignRefer getForeignReference() {
        return this;
    }

    @Override
    public FieldRawInfo getFieldRawInfo() {
        return fieldRawInfo;
    }

    @Override
    public boolean containNotNullConstraint() {
        return flags[4];
    }

    @Override
    public boolean isUnique() {
        return flags[3];
    }

    @Override
    public boolean isPrimaryKey() {
        return flags[0];
    }

    @Override
    public boolean isAutoIncrement() {
        return flags[2];
    }

    @Override
    public boolean isStable() {
        return flags[1];
    }

    @Override
    public ColumnInfo getColumnInfo() {
        return null;
    }
}

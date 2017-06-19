package com.example.tablemetabuilder.impl;

import com.example.tablemeta.ColumnInfo;
import com.example.tablemeta.FieldRawInfo;
import com.example.tablemeta.ForeignRefer;

/**
 * Created by RamenBird on 2017/5/19.
 */

class ColumnInfoImpl implements ColumnInfo, ForeignRefer {
    ColumnInfoImpl foreignColumn;
    String columnName;
    FieldRawInfoImpl field;

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
        return null;
    }

    @Override
    public boolean containNotNullConstraint() {
        return false;
    }

    @Override
    public boolean isUnique() {
        return false;
    }

    @Override
    public boolean isPrimaryKey() {
        return false;
    }

    @Override
    public boolean isAutoIncrement() {
        return false;
    }

    @Override
    public ColumnInfo getColumnInfo() {
        return foreignColumn;
    }
}

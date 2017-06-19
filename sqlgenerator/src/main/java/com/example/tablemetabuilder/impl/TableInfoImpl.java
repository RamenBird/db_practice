package com.example.tablemetabuilder.impl;

import com.example.tablemeta.ColumnInfo;
import com.example.tablemeta.ClassRawInfo;
import com.example.tablemeta.TableInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RamenBird on 2017/1/12.
 * <p>
 * This class is a wrapper of a certain dto class model.
 * <p>
 * No, it's not.
 */


class TableInfoImpl implements TableInfo {
    ClassRawInfoImpl mDbClass;
    String tableName;
    List<ColumnInfo> columns = new ArrayList<>();

    @Override
    public ClassRawInfo getClassRawInfo() {
        return mDbClass;
    }

    @Override
    public List<ColumnInfo> getColumns() {
        return columns;
    }

    @Override
    public String getTableName() {
        return tableName;
    }
}

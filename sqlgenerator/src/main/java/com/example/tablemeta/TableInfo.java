package com.example.tablemeta;

import java.util.List;

/**
 * Created by RamenBird on 2017/5/5.
 */

public interface TableInfo {
    ClassRawInfo getClassRawInfo();

    List<ColumnInfo> getColumns();

    String getTableName();
}

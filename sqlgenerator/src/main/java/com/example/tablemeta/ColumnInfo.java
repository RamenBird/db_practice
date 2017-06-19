package com.example.tablemeta;

/**
 * Created by RamenBird on 2017/5/5.
 */

public interface ColumnInfo {
    String getColumnName();

    ForeignRefer getForeignReference();

    FieldRawInfo getFieldRawInfo();

    boolean containNotNullConstraint();

    boolean isUnique();

    boolean isPrimaryKey();

    boolean isAutoIncrement();
}

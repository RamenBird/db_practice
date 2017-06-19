package com.example.tablemetabuilder.impl;

import com.example.tablemeta.FieldRawInfo;

/**
 * Created by RamenBird on 2017/1/19.
 */

class FieldRawInfoImpl implements FieldRawInfo {
    String getterName;
    String setterName;
    String fieldName;

    @Override
    public String getGetterMethodName() {
        return getterName;
    }

    @Override
    public String getSetterMethodName() {
        return setterName;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }
}

package com.example.tablemetabuilder.impl;

import com.example.tablemeta.FieldRawInfo;

import javax.lang.model.element.Element;

/**
 * Created by RamenBird on 2017/1/19.
 */

class FieldRawInfoImpl implements FieldRawInfo {
    String getterName;
    String setterName;
    String fieldName;
    Element rawElement;

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

    @Override
    public Element getFieldElement() {
        return rawElement;
    }
}

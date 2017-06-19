package com.example.tablemetabuilder.impl;

import com.example.tablemeta.ClassRawInfo;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Element;

/**
 * Created by RamenBird on 2017/1/3.
 */

class ClassRawInfoImpl implements ClassRawInfo {
    private String mClassFullPath;
    private Element mElement;
    private String mTableName;

    public ClassRawInfoImpl(Element element, String className, String classFullPath) {
        mElement = element;
        mTableName = className;
        mClassFullPath = classFullPath;
    }

    @Override
    public String getClassName() {
        return mTableName;
    }

    @Override
    public String getClassFullName() {
        return mClassFullPath;
    }

    @Override
    public Element getRawTypeElement() {
        return mElement;
    }
}

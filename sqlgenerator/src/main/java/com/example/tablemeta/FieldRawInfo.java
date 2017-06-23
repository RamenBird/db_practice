package com.example.tablemeta;

import javax.lang.model.element.Element;

/**
 * Created by RamenBird on 2017/5/15.
 */

public interface FieldRawInfo {
    String getGetterMethodName();

    String getSetterMethodName();

    String getFieldName();

    Element getFieldElement();
}

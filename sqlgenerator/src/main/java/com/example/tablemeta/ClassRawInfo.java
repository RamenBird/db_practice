package com.example.tablemeta;

import javax.lang.model.element.Element;

/**
 * Created by RamenBird on 2016/12/29.
 */

public interface ClassRawInfo {
    String getClassName();

    String getClassFullName();

    Element getRawTypeElement();
}

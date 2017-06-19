package com.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by RamenBird on 2017/1/24.
 */

@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.SOURCE)
public @interface Foreign {
    Class<?> table();

    String field();
}

package com.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by RamenBird on 2017/1/17.
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface Column {
    String setter() default "";

    String getter() default "";

    boolean ignore() default false;

    boolean stable() default false;

    boolean notNull() default false;

    boolean unique() default false;

    boolean primary() default false;

    boolean autoIncrement() default false;
}

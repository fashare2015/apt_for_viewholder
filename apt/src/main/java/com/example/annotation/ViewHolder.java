package com.example.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Created by apple on 16-11-19.
 */
@Target(TYPE)
@Retention(CLASS)
public @interface ViewHolder {
    int layoutRes();
    Field[] fields() default {};
}

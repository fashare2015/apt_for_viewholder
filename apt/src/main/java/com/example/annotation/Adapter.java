package com.example.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Created by apple on 16-11-19.
 */
@Target(FIELD)
@Retention(CLASS)
public @interface Adapter {
    String moduleName() default "";
    ViewHolder viewHolder();
}

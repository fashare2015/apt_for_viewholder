package com.example.javapoet;

import com.squareup.javapoet.MethodSpec;

/**
 * User: fashare(153614131@qq.com)
 * Date: 2016-11-19
 * Time: 23:10
 * <br/><br/>
 */
public abstract class MethodBuilder<Data> extends AbstractBuilder<Data, MethodSpec> {

    public MethodBuilder(Data data) {
        super(data);
    }
}
package com.example.javapoet;

import com.squareup.javapoet.TypeSpec;

/**
 * User: fashare(153614131@qq.com)
 * Date: 2016-11-20
 * Time: 04:32
 * <br/><br/>
 */
public abstract class ClassBuilder<Data> extends AbstractBuilder<Data, TypeSpec> {
    public ClassBuilder(Data data) {
        super(data);
        System.out.println(this.getClass().getSimpleName() + ": " + getData());
    }

}
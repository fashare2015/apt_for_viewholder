package com.example.javapoet;

/**
 * User: fashare(153614131@qq.com)
 * Date: 2016-11-19
 * Time: 23:10
 * <br/><br/>
 */
public abstract class AbstractBuilder<Data, Output> implements Builder<Output> {
    private Data mData;

    public Data getData() {
        return mData;
    }

    public AbstractBuilder(Data data) {
        mData = data;
    }
}
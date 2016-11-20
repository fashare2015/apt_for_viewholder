package com.example.javapoet;

import com.example.annotation.info.ClassInfo;
import com.squareup.javapoet.JavaFile;

/**
 * User: fashare(153614131@qq.com)
 * Date: 2016-11-19
 * Time: 23:10
 * <br/><br/>
 */
public class FileBuilder extends AbstractBuilder<String, JavaFile> {
    ClassBuilder<? extends ClassInfo> mBuilder;

    public FileBuilder(String packageName, ClassBuilder<? extends ClassInfo> builder) {
        super(packageName);
        mBuilder = builder;
    }

    @Override
    public JavaFile build() {
        return JavaFile.builder(
                getData(),
                mBuilder.build()
        ).build();
    }
}
package com.example.annotation.info;

import com.annimon.stream.Stream;
import com.example.MyProcessor;
import com.example.javapoet.ClassBuilder;

import java.lang.annotation.Annotation;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;

/**
 * User: fashare(153614131@qq.com)
 * Date: 2016-11-19
 * Time: 22:48
 * <br/><br/>
 */
public abstract class ClassInfo<E extends Element, A extends Annotation> {
    E mAnnotatedElement;
    A mAnnotation;

    public E getAnnotatedElement() {
        return mAnnotatedElement;
    }

    public A getAnnotation() {
        return mAnnotation;
    }

    public ClassInfo(E element) {
        mAnnotatedElement = element;
        mAnnotation = mAnnotatedElement.getAnnotation(getAnnotationClazz());
    }

    public ClassInfo(E element, A annotation) {
        mAnnotatedElement = element;
        mAnnotation = annotation;
    }

    protected abstract Class<A> getAnnotationClazz();

    public abstract String getSimpleName();

    public String getPackageName(){
        return Stream.of(getAnnotatedElement())
                .map(MyProcessor.sElementUtils:: getPackageOf)
                .map(PackageElement:: getQualifiedName)
                .map(Name:: toString)
                .findFirst().orElse("com.fashare");
    }

    @Override
    public String toString() {
        return getSimpleName();
    }

    public abstract ClassBuilder<? extends ClassInfo> getClassBuilder();

}
package com.example.annotation.info;


import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.example.annotation.Field;
import com.example.javapoet.AbstractBuilder;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;

import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.MirroredTypeException;


/**
 * User: fashare(153614131@qq.com)
 * Date: 2016-11-19
 * Time: 22:48
 * <br/><br/>
 */
public class FieldInfo {
    Field mField;

    public FieldInfo(Field field) {
        mField = field;
    }

    public TypeName getClazz() {
        return getClazz(mField);
    }

    /**
     * solution:
     * http://stackoverflow.com/questions/7687829/java-6-annotation-processing-getting-a-class-from-an-annotation
     */
    private static TypeName getClazz(Field annotation) {
        try {
            return TypeName.get(annotation.clazz()); // this should throw
        } catch( MirroredTypeException mte ) {
            return TypeName.get(mte.getTypeMirror());
        }
    }

    public int getIdRes() {
        return mField.idRes();
    }

    public String getName() {
        return !mField.name().equals("")? mField.name():
                "m" + ((ClassName)getClazz()).simpleName() + "_" + mField.idRes();
    }

    /**
     * User: fashare(153614131@qq.com)
     * Date: 2016-11-19
     * Time: 23:10
     * <br/><br/>
     */
    public static class Builder extends AbstractBuilder<List<FieldInfo>, List<FieldSpec>> {

        public Builder(List<FieldInfo> data) {
            super(data);
        }

        @Override
        public List<FieldSpec> build() {
            return Stream.of(getData())
                    .map(it -> FieldSpec.builder(it.getClazz(), it.getName(), Modifier.PUBLIC).build())
                    .collect(Collectors.toList());
        }
    }
}
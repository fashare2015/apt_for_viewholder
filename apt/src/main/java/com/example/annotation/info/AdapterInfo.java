package com.example.annotation.info;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.example.MyProcessor;
import com.example.annotation.Adapter;
import com.example.constant.ClassNames;
import com.example.javapoet.ClassBuilder;
import com.example.javapoet.MethodBuilder;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * User: fashare(153614131@qq.com)
 * Date: 2016-11-20
 * Time: 06:08
 * <br/><br/>
 */
public class AdapterInfo extends ClassInfo<VariableElement, Adapter>{
    public static final String POSTFIX = "$$Adapter";  // 实现类的后缀
    private final ViewHolderInfo mViewHolderInfo;

    private TypeName getViewHolderName() {
        return mViewHolderInfo.getTypeName();
    }

    public AdapterInfo(VariableElement it) {
        super(it);
        mViewHolderInfo = new ViewHolderInfo((TypeElement) it.getEnclosingElement(), getAnnotation().viewHolder());
        MyProcessor.generateJavaCode(Arrays.asList(mViewHolderInfo));
    }

    @Override
    protected Class<Adapter> getAnnotationClazz() {
        return Adapter.class;
    }

    @Override
    public String getSimpleName() {
        return getAnnotatedElement().getSimpleName() + POSTFIX;
    }

    @Override
    public ClassBuilder<? extends ClassInfo> getClassBuilder() {
        return new Clazz(this);
    }

    private static class Clazz extends ClassBuilder<AdapterInfo> {
        public Clazz(AdapterInfo adapterInfo) {
            super(adapterInfo);
        }

        @Override
        public TypeSpec build() {
            return TypeSpec.classBuilder(getData().getSimpleName())   // 类名
                    .superclass(ParameterizedTypeName.get(ClassNames.ADAPTER, getData().getViewHolderName()))    // 父类
                    .addModifiers(Modifier.PUBLIC)  // 修饰符
                    .addMethods(Stream.of(getMethodBuilderList())
                            .map(MethodBuilder:: build)
                            .collect(Collectors.toList())
                    )   // 方法
                    .addField(ClassNames.CONTEXT, "mContext")
                    .addField(List.class, "mDataList")
                    .build();
        }

        protected List<? extends MethodBuilder<AdapterInfo>> getMethodBuilderList() {
            return Arrays.asList(
                    new SetDataList(getData()),
                    new Constructor(getData()),
                    new OnCreateViewHolder(getData()),
                    new OnBindViewHolder(getData()),
                    new GetItemCount(getData())
            );
        }
    }

    private static class Constructor extends MethodBuilder<AdapterInfo> {
        public Constructor(AdapterInfo it) {
            super(it);
        }

        @Override
        public MethodSpec build() {
            return MethodSpec.constructorBuilder()  // 构造函数
                    .addModifiers(Modifier.PUBLIC)  // public
                    .addParameter(ClassNames.CONTEXT, "context") // 参数
                    .addStatement(
                            "mContext = context;\n" +
                            "mDataList = new $T()",
                            ArrayList.class
                    ).build();   // 实现体
        }
    }

    private static class SetDataList extends MethodBuilder<AdapterInfo> {
        public SetDataList(AdapterInfo it) {
            super(it);
        }

        @Override
        public MethodSpec build() {
            return MethodSpec.methodBuilder("setDataList")
                    .addModifiers(Modifier.PUBLIC)  // public
                    .addParameter(List.class, "dataList") // 参数
                    .addStatement(
                            "mDataList = dataList;\n" +
                            "notifyDataSetChanged()"
                    ).build();
        }
    }

    private static class OnCreateViewHolder extends MethodBuilder<AdapterInfo> {
        public OnCreateViewHolder(AdapterInfo it) {
            super(it);
        }

        @Override
        public MethodSpec build() {
            return MethodSpec.methodBuilder("onCreateViewHolder")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)  // public
                    .returns(getData().getViewHolderName())
                    .addParameter(ClassNames.VIEW_GROUP, "viewgroup") // 参数
                    .addParameter(int.class, "viewType")
                    .addStatement(
                            "return new $T(mContext)",
                            getData().getViewHolderName()
                    ).build();
        }
    }

    private static class OnBindViewHolder extends MethodBuilder<AdapterInfo> {
        public OnBindViewHolder(AdapterInfo it) {
            super(it);
        }

        @Override
        public MethodSpec build() {
            return MethodSpec.methodBuilder("onBindViewHolder")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)  // public
                    .addParameter(getData().getViewHolderName(), "holder") // 参数
                    .addParameter(int.class, "position")
                    .addStatement(
                            "holder.bind((String)mDataList.get(position))"
                    ).build();
        }
    }

    private static class GetItemCount extends MethodBuilder<AdapterInfo> {
        public GetItemCount(AdapterInfo it) {
            super(it);
        }

        @Override
        public MethodSpec build() {
            return MethodSpec.methodBuilder("getItemCount")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)  // public
                    .returns(int.class)
                    .addStatement(
                            "return mDataList!=null? mDataList.size(): 0"
                    ).build();
        }
    }
}
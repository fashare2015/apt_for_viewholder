package com.example.annotation.info;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.example.annotation.ViewHolder;
import com.example.constant.ClassNames;
import com.example.javapoet.ClassBuilder;
import com.example.javapoet.MethodBuilder;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.Arrays;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * User: fashare(153614131@qq.com)
 * Date: 2016-11-19
 * Time: 22:48
 * <br/><br/>
 */
public class ViewHolderInfo extends ClassInfo<TypeElement, ViewHolder>{
    public static final String POSTFIX = "$$ViewHolder";  // 实现类的后缀

    public ViewHolderInfo(TypeElement typeElement) {
        super(typeElement);
    }

    public ViewHolderInfo(TypeElement element, ViewHolder annotation) {
        super(element, annotation);
    }

    @Override
    protected Class<ViewHolder> getAnnotationClazz() {
        return ViewHolder.class;
    }

    @Override
    public String getSimpleName() {
        return getAnnotatedElement().getSimpleName() + POSTFIX;
    }

    @Override
    public ClassBuilder<? extends ClassInfo> getClassBuilder() {
        return new Clazz(this);
    }

    public int getLayoutRes() {
        return getAnnotation().layoutRes();
    }

    public List<FieldInfo> getFieldInfos() {
        return Stream.of(getAnnotation().fields())
                .map(FieldInfo:: new)
                .collect(Collectors.toList());
    }

    public TypeName getTypeName() {
        return ClassName.get(getPackageName(), getSimpleName());
    }

    private static class Clazz extends ClassBuilder<ViewHolderInfo> {

        public Clazz(ViewHolderInfo viewHolderInfo) {
            super(viewHolderInfo);
        }

        @Override
        public TypeSpec build() {
            System.out.println("build: " + getData().getSimpleName());
            return TypeSpec.classBuilder(getData().getSimpleName())   // 类名
                    .superclass(ClassNames.VIEW_HOLDER)    // 父类
                    .addModifiers(Modifier.PUBLIC)  // 修饰符
                    .addMethods(Stream.of(getMethodBuilderList())
                            .map(MethodBuilder:: build)
                            .collect(Collectors.toList())
                    )   // 方法
                    .addFields(new FieldInfo.Builder(getData().getFieldInfos()).build())   // 遍历 @Field, 依次生成 field 变量
                    .build();
        }

        protected List<? extends MethodBuilder<ViewHolderInfo>> getMethodBuilderList() {
            return Arrays.asList(
                    new Constructor(getData()),
                    new Bind(getData())
            );
        }
    }

    private static class Bind extends MethodBuilder<ViewHolderInfo> {

        public Bind(ViewHolderInfo viewHolderInfo) {
            super(viewHolderInfo);
        }

        @Override
        public MethodSpec build() {
            MethodSpec.Builder builder = MethodSpec.methodBuilder("bind")  // 构造函数
                    .addModifiers(Modifier.PUBLIC)  // public
                    .addParameter(String.class, "data"); // 参数

            // 遍历 @Field, 依次生成 setText()
            Stream.of(getData().getFieldInfos())
                    .forEach(it -> builder.addStatement(
                            "$L.setText(data)",
                            it.getName()
                    ));

            return builder.build();
        }
    }

    private static class Constructor extends MethodBuilder<ViewHolderInfo> {

        public Constructor(ViewHolderInfo viewHolderInfo) {
            super(viewHolderInfo);
        }

        @Override
        public MethodSpec build() {
            MethodSpec.Builder builder = MethodSpec.constructorBuilder()  // 构造函数
                    .addModifiers(Modifier.PUBLIC)  // public
                    .addParameter(ClassNames.CONTEXT, "context") // 参数
                    .addStatement(
                            "super($T.inflate(context, $L, null))",
                            ClassNames.VIEW, getData().getLayoutRes()
                    );   // 实现体

            // 遍历 @Field, 依次生成 findVIewById()
            Stream.of(getData().getFieldInfos())
                    .forEach(it -> builder.addStatement(
                            "$L = ($T)itemView.findViewById($L)",
                            it.getName(), it.getClazz(), it.getIdRes()
                    ));

            return builder.build();
        }
    }
}
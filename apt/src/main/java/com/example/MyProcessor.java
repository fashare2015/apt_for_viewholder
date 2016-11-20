package com.example;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.example.annotation.Adapter;
import com.example.annotation.ViewHolder;
import com.example.annotation.info.AdapterInfo;
import com.example.annotation.info.ClassInfo;
import com.example.annotation.info.ViewHolderInfo;
import com.example.javapoet.FileBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

/**
 * User: fashare(153614131@qq.com)
 * Date: 2016-11-19
 * Time: 22:23
 * <br/><br/>
 */
//@AutoService(Processor.class)
@SupportedAnnotationTypes({
        "com.example.annotation.ViewHolder",
        "com.example.annotation.Adapter"
})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class MyProcessor extends AbstractProcessor{

    public static Elements sElementUtils;
    private static Filer sFiler;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        sElementUtils = processingEnv.getElementUtils();
        sFiler = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("process: ");
        Stream.of(annotations).forEach(System.out:: println);

        List<ClassInfo> infoList = getClassInfoList(annotations, roundEnv);
        generateJavaCode(infoList);

        return true;
    }

    private List<ClassInfo> getClassInfoList(Set<? extends TypeElement> annotations,
                                             RoundEnvironment roundEnv) {
        return Stream.of(annotations)
                // 1. 变换: 获取所有被 ViewHolder 标注的对象: Set<Element>
                .map(roundEnv:: getElementsAnnotatedWith)
                // 2. 展平: 去一层嵌套Stream<Set<Element>> 转换为 Stream<Element>
                .flatMap(Stream:: of)
                // 3. 变换: 转换成 ClassInfo
                .map(it -> {
                    if(it.getAnnotation(ViewHolder.class) != null) {
                        return new ViewHolderInfo((TypeElement) it);
                    }else if(it.getAnnotation(Adapter.class) != null)
                        return new AdapterInfo((VariableElement) it);
                    return null;
                })
                .filter(it -> it!=null)
                // 4. 收集: List<ClassInfo>
                .collect(Collectors.toList());
    }

    public static void generateJavaCode(List<ClassInfo> infoList) {
        System.out.println("generateJavaCode: " + infoList.toString());
        Stream.of(infoList).forEach(it -> {
            try {
                new FileBuilder(it.getPackageName(), it.getClassBuilder())
                        .build()
                        .writeTo(sFiler);
            }catch (IOException e){
                System.out.println(e.getMessage());
            }
        });
        System.out.println("generateJavaCode: end");
    }
}
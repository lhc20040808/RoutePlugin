package com.router.plugin.processor;

import com.google.auto.service.AutoService;
import com.router.plugin.annotation.Destination;

import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

@AutoService(Processor.class)
public class DestinationProcessor extends AbstractProcessor {

    private static final String TAG = "[DestinationProcessor]";

    /**
     * 告诉Javac编译器当前处理器支持的注解类型
     *
     * @return 需要处理的注解列表
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(Destination.class.getCanonicalName());
    }

    /**
     * 编译器找到注解后会回调这个方法
     *
     * @param set              编译器收集到的注解信息
     * @param roundEnvironment
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        System.out.println(TAG + "process start");
        //获取所有标记了@Destination注解的类的信息
        Set<Element> allDestinationElements = (Set<Element>) roundEnvironment.getElementsAnnotatedWith(Destination.class);
        System.out.println(TAG + "all Destination elements count = " + allDestinationElements.size());
        //当未搜集@Destination注解的类，跳过后续流程
        if (allDestinationElements.isEmpty()) {
            return false;
        }
        //遍历所有注解信息
        for (Element element : allDestinationElements) {
            final TypeElement typeElement = (TypeElement) element;
            final Destination destination = typeElement.getAnnotation(Destination.class);
            if (destination == null) {
                continue;
            }
            final String url = destination.url();
            final String des = destination.des();
            final String realPath = typeElement.getQualifiedName().toString();
            System.out.println(TAG + des + "|" + url + "|" + realPath);
        }
        System.out.println(TAG + "process finish");
        return false;
    }
}

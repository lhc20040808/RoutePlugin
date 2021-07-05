package com.router.plugin.processor;

import com.google.auto.service.AutoService;
import com.router.plugin.annotation.Destination;

import java.io.Writer;
import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

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

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
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
        //避免多次调用
        if (roundEnvironment.processingOver()) {
            return false;
        }
        System.out.println(TAG + "process start. Thread " + Thread.currentThread().getName());
        //获取所有标记了@Destination注解的类的信息
        Set<Element> allDestinationElements = (Set<Element>) roundEnvironment.getElementsAnnotatedWith(Destination.class);
        System.out.println(TAG + "all Destination elements count = " + allDestinationElements.size());
        //当未搜集@Destination注解的类，跳过后续流程
        if (allDestinationElements.isEmpty()) {
            return false;
        }
        //将要自动生成的类名
        String className = "RouterMapping_" + System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append("package com.marco.route.plugin;\n\n");
        sb.append("import java.util.HashMap;\n");
        sb.append("import java.util.Map;\n\n");
        sb.append("public class ").append(className).append(" {\n\n");
        sb.append("     public static Map<String,String> get() {\n");
        sb.append("         Map<String,String> mapping = new HashMap<>();\n");

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
            sb.append("         ")
                    .append("mapping.put(")
                    .append("\"").append(url).append("\"").append(", ")
                    .append("\"").append(realPath).append("\"").append(");\n");
        }
        sb.append("         return mapping;\n");
        sb.append("     }\n");
        sb.append("}\n");
        String mappingFullName = "com.marco.route.plugin." + className;
        System.out.println(TAG + "fullClassName " + mappingFullName);
        System.out.println(TAG + "class content\n" + sb);
        //写入自动生成的类导本地文件中
        try {
            JavaFileObject source = processingEnv.getFiler().createSourceFile(mappingFullName);
            Writer writer = source.openWriter();
            writer.write(sb.toString());
            writer.flush();
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException("Error while create file", e);
        }

        System.out.println(TAG + "process finish");
        return false;
    }
}

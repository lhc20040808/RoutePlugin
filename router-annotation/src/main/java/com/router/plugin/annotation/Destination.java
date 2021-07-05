package com.router.plugin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Destination {
    /**
     * 当前页面Url
     * 不能为空
     *
     * @return 页面Url
     */
    String url();

    /**
     * @return 页面描述
     */
    String des() default "";
}

package com.marco.router.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class RouterPlugin implements Plugin<Project> {

    /**
     * 当工程用apply关键字引用插件的时候，apply方法会被执行
     * 编译JAVA版本过高可能会引起报错
     * @param project
     */
    @Override
    void apply(Project project) {
        println("[RouterPlugin]apply from ${project.name}")
        //step1 定义Extension
        //step2 注册Extension
        //step3 使用Extension
        //step4 获取Extension
        project.getExtensions().create("router", RouterExtension)
        project.afterEvaluate {
            //当前工程配置阶段结束，此处可以拿到用户配置的Extension参数
            RouterExtension extension = project["router"]
            println("用户设置的wiki路径:${extension.wikiDir}")
        }
    }
}
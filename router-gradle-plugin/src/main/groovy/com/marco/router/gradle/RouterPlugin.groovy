package com.marco.router.gradle

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import groovy.json.JsonSlurper

class RouterPlugin implements Plugin<Project> {

    /**
     * 当工程用apply关键字引用插件的时候，apply方法会被执行
     * 编译JAVA版本过高可能会引起报错
     * @param project
     */
    @Override
    void apply(Project project) {
        //判断工程是否有App插件，如果有则说明该工程是一个com.android.application的工程
        if (project.plugins.hasPlugin(AppPlugin)) {
            AppExtension appExtension = project.extensions.getByType(AppExtension)
            RouterMappingTransform mappingTransform = new RouterMappingTransform()
            //注册TransForm
            appExtension.registerTransform(mappingTransform)
        }

        //自动帮助用户传递路径参数到注解处理器中
        if (project.extensions.findByName("kapt") != null) {
            project.extensions.findByName("kapt").arguments {
                arg("root_project_dir", project.rootProject.projectDir.absolutePath)
            }
        }

        //实现旧的构建产物的自动清理
        project.clean.doFirst {
            File routerMapping = new File(project.rootProject.projectDir, "router_mapping")
            if (routerMapping.exists()) {
                routerMapping.deleteDir()
            }
        }

        if (!project.plugins.hasPlugin(AppPlugin)) {
            return
        }
        //注册Extension
        project.getExtensions().create("router", RouterExtension)
        project.afterEvaluate {
            //找到java任务
            //当前工程配置阶段结束，此处可以拿到用户配置的Extension参数
            RouterExtension extension = project["router"]
            println("[RouterPlugin]用户设置的wiki路径:${extension.wikiDir}")
            project.tasks.findAll { task ->
                //在javac任务后汇总生成文档
                task.name.startsWith('compile') && task.name.endsWith('JavaWithJavac')
            }.each { task ->
                println("[RouterPlugin]findTask:$task.name")
                task.doLast {
                    createMarkDown(project, extension.wikiDir)
                }
            }
        }
    }

    private static void createMarkDown(Project project, String dir) {
        println("[RouterPlugin]createMarkDown")
        File routerMapping = new File(project.rootProject.projectDir,
                "router_mapping")
        if (!routerMapping.exists()) {
            return
        }
        File[] allChildFiles = routerMapping.listFiles()
        if (allChildFiles.size() < 1) {
            return
        }
        StringBuilder markdownBuilder = new StringBuilder()
        markdownBuilder.append("# 页面文档\n\n")
        markdownBuilder.append("|DESCRIPTION|URL|REAL PATH|\n")
        markdownBuilder.append("|-|-|-|\n")
        allChildFiles.each { child ->
            if (child.name.endsWith('.json')) {
                JsonSlurper jsonSlurper = new JsonSlurper()
                def content = jsonSlurper.parse(child)
                content.each { innerContent ->
                    def url = innerContent['url']
                    def des = innerContent['des']
                    def realPath = innerContent['realPath']
                    markdownBuilder.append("|").append(des)
                            .append("|").append(url)
                            .append("|").append(realPath)
                            .append("|").append("\n")
                }
            }
        }

        File wikiFileDir = new File(dir)
        if (!wikiFileDir.exists()) {
            wikiFileDir.mkdir()
        }
        File wikiFile = new File(wikiFileDir, "页面文档.md")
        if (wikiFile.exists()) {
            wikiFile.delete()
        }
        wikiFile.write(markdownBuilder.toString())
    }
}
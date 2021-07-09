package com.marco.router.gradle

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils

import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

class RouterMappingTransform extends Transform {

    /**
     * 返回当前Transform名称
     * @return
     */
    @Override
    String getName() {
        return "RouterMappingTransform"
    }

    /**
     * 告诉编译器，当前Transform需要处理的类型
     * @return
     */
    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    /**
     * 告诉编译器，当前Transform需要收集的范围
     * @return
     */
    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    /**
     * 告诉编译器，Transform是否支持增量
     * @return
     */
    @Override
    boolean isIncremental() {
        return false
    }

    /**
     * 当编译器收集好所有class，会打包生成一个TransformInvocation
     * 编译器会把这个生成的TransformInvocation传入该方法
     * @param transformInvocation
     * @throws TransformException* @throws InterruptedException* @throws IOException
     */
    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        //遍历所有的Input,将Input拷贝到目标目录(TransForm必做的事情)
        RouterMappingCollector collector = new RouterMappingCollector()

        transformInvocation.inputs.each {
            //把文件夹类型的输入拷贝到目标目录
            it.directoryInputs.each {
                directory ->
                    def destDir = transformInvocation.outputProvider.getContentLocation(
                            directory.name,
                            directory.contentTypes,
                            directory.scopes,
                            Format.DIRECTORY)
                    collector.collect(directory.file)
                    FileUtils.copyDirectory(directory.file, destDir)
            }
            //把Jar类型的输入拷贝到目标目录
            it.jarInputs.each { jarInput ->
                def dest = transformInvocation.outputProvider.getContentLocation(
                        jarInput.name,
                        jarInput.contentTypes,
                        jarInput.scopes,
                        Format.JAR)
                collector.collectFromJarFile(jarInput.file)
                FileUtils.copyFile(jarInput.file, dest)
            }

        }
        //对Input进行二次处理
        println("[${getName()}] all mapping class name =" + collector.getMappingClassNames())

        File mappingJarFile = transformInvocation.outputProvider.getContentLocation("router_mapping",
                getOutputTypes(),
                getScopes(),
                Format.JAR)
        if (!mappingJarFile.getParentFile().exists()) {
            mappingJarFile.getParentFile().mkdirs()
        }

        if (mappingJarFile.exists()) {
            mappingJarFile.delete()
        }

        //将生成的字节码写入本地文件
        println("[${getName()}] mappingJarFile = ${mappingJarFile}")
        FileOutputStream fos = new FileOutputStream(mappingJarFile)
        JarOutputStream jarOutputStream = new JarOutputStream(fos)
        ZipEntry zipEntry = new ZipEntry(RouterMappingByteCodeBuilder.CLASS_NAME + ".class")
        jarOutputStream.putNextEntry(zipEntry)
        jarOutputStream.write(RouterMappingByteCodeBuilder.get(collector.getMappingClassNames()))
        jarOutputStream.closeEntry()
        jarOutputStream.close()
        fos.close()
    }
}
package com.marco.router.gradle

import java.util.jar.JarEntry
import java.util.jar.JarFile

class RouterMappingCollector {

    private static final String PACKAGE_NAME = 'com/marco/route/plugin'
    private static final String CLASS_PREFIX = 'RouterMapping_'
    private static final String CLASS_SUFFIX = '.class'

    private final Set<String> mappingClassName = new HashSet<>()

    /**
     * 获取收集好的映射表类名
     * @return
     */
    Set<String> getMappingClassNames() {
        return mappingClassName
    }

    /**
     * 收集class文件或者class文件目录中的映射表类
     * @param classFile
     */
    void collect(File classFile) {
        if (classFile == null || !classFile.exists()) {
            return
        }
        if (classFile.isFile()) {
            if (classFile.absolutePath.contains(PACKAGE_NAME)
                    && classFile.name.startsWith(CLASS_PREFIX)
                    && classFile.name.endsWith(CLASS_SUFFIX)) {
                String className = classFile.name.replace(CLASS_SUFFIX, "")
                mappingClassName.add(className)
            }
        } else {
            classFile.listFiles().each {
                collect(it)
            }
        }
    }

    /**
     * 收集JAR包中的目标类
     */
    void collectFromJarFile(File jarFile) {
        Enumeration enumeration = new JarFile(jarFile).entries()
        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) enumeration.nextElement()
            String entryName = jarEntry.getName()
            if (entryName.contains(PACKAGE_NAME)
                    && entryName.contains(CLASS_PREFIX)
                    && entryName.contains(CLASS_SUFFIX)) {
                String className = entryName.replace(PACKAGE_NAME, "")
                        .replace("/", "")
                        .replace(CLASS_SUFFIX, "")
                mappingClassName.add(className)
            }
        }
    }
}
package com.marco.router.gradle

import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class RouterMappingByteCodeBuilder implements Opcodes {
    public static final String CLASS_NAME = "com/marco/route/mapping/generated/RouterMapping"

    static byte[] get(Set<String> allMappingNames) {
        //1.创建一个类
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES)
        classWriter.visit(V1_8,
                ACC_PUBLIC + ACC_SUPER,
                CLASS_NAME,
                null,
                "java/lang/Object",
                null
        )
        //2.创建构造方法
        MethodVisitor methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null)
        methodVisitor.visitCode()
        methodVisitor.visitVarInsn(ALOAD, 0)
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
        methodVisitor.visitInsn(RETURN);
        methodVisitor.visitMaxs(1, 1)
        methodVisitor.visitEnd()
        //3.创建get方法
        //  (1)创建一个Map
        methodVisitor = classWriter.visitMethod(ACC_PUBLIC | ACC_STATIC,
                "get",
                "()Ljava/util/Map;",
                "()Ljava/util/Map<Ljava/lang/String;Ljava/lang/String;>;",
                null)
        methodVisitor.visitCode()

        methodVisitor.visitTypeInsn(NEW, "java/util/HashMap")
        methodVisitor.visitInsn(DUP)
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/util/HashMap", "<init>", "()V", false)
        methodVisitor.visitVarInsn(ASTORE, 0)
        //  (2)塞入所有映射表的内容
        allMappingNames.each {
            methodVisitor.visitVarInsn(ALOAD, 0)
            methodVisitor.visitMethodInsn(INVOKESTATIC,
                    "com/marco/route/plugin/$it",
                    "get",
                    "()Ljava/util/Map;",
                    false)
            methodVisitor.visitMethodInsn(INVOKEINTERFACE,
                    "java/util/Map",
                    "putAll",
                    "(Ljava/util/Map;)V",
                    true)
        }
        //  (3)返回map
        methodVisitor.visitVarInsn(ALOAD, 0)
        methodVisitor.visitInsn(ARETURN)
        methodVisitor.visitMaxs(2, 2)
        methodVisitor.visitEnd()
        return classWriter.toByteArray()
    }
}
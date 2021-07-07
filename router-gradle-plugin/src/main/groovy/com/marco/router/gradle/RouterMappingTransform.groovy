package com.marco.router.gradle

import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation

class RouterMappingTransform extends Transform {

    /**
     * 返回当前Transform名称
     * @return
     */
    @Override
    String getName() {
        return null
    }

    /**
     * 告诉编译器，当前Transform需要处理的类型
     * @return
     */
    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return null
    }

    /**
     * 告诉编译器，当前Transform需要收集的范围
     * @return
     */
    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return null
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
        super.transform(transformInvocation)
    }
}
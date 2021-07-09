package com.marco.router.runtime

import android.util.Log

object Router {
    private const val TAG = "RouterTAG"
    private const val GENERATED_CLASS_NAME = "com.marco.route.mapping.generated.RouterMapping"
    private val mapping: HashMap<String, String> = HashMap()

    fun init() {
        val clz = Class.forName(GENERATED_CLASS_NAME)
        val method = clz.getDeclaredMethod("get")
        val allMapping = method.invoke(null) as Map<String, String>
        if (allMapping.isNotEmpty()) {
            Log.i(TAG, "init success. Get all mapping")
            allMapping.entries.forEach {
                Log.i(TAG, "${it.key}->${it.value}")
            }
            mapping.putAll(allMapping)
        }
    }
}
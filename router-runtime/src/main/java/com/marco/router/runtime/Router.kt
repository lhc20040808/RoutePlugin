package com.marco.router.runtime

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
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
            allMapping.onEach {
                Log.i(TAG, "${it.key}->${it.value}")
            }
            mapping.putAll(allMapping)
        }
    }

    fun go(context: Context?, url: String?) {
        if (context == null || url == null) {
            return
        }
        //匹配Url,找到目标页面
        //router://home/main
        val uri = Uri.parse(url)
        val schema = uri.scheme
        val host = uri.host
        val path = uri.path
        var target = ""

        mapping.onEach {
            val key = it.key
            val tUri = Uri.parse(key)
            val tSchema = tUri.scheme
            val tHost = tUri.host
            val tPath = tUri.path
            if (schema == tSchema && host == tHost && path == tPath) {
                target = it.value
            }
        }

        if (TextUtils.isEmpty(target)) {
            Log.e(TAG, "destination [$target] not found")
            return
        }

        //解析参数
        val bundle = Bundle()
        val parameterNames = uri.queryParameterNames
        parameterNames.forEach {
            bundle.putString(it, uri.getQueryParameter(it))
        }

        //打开对应的Activity，并传入参数
        try {
            val activity = Class.forName(target)
            val intent = Intent(context, activity)
            intent.putExtras(bundle)
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e(TAG, "error while start activity: $target")
        }
    }
}
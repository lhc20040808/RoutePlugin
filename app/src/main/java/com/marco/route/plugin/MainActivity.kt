package com.marco.route.plugin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.marco.router.runtime.Router
import com.router.plugin.annotation.Destination

@Destination(url = "router://home/main", des = "应用主页")
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Router.init()
        findViewById<TextView>(R.id.tvOpenRead).setOnClickListener {
            Router.go(it.context, "router://read/reading?prdCode=100&prdName=雷雨")
        }
    }
}
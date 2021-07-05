package com.marco.route.plugin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.router.plugin.annotation.Destination

@Destination(url = "router://home/main", des = "应用主页")
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
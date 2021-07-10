package com.marco.biz.reading

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.router.plugin.annotation.Destination

@Destination(url = "router://read/reading", des = "阅读-阅读页")
class ReadingActivity : AppCompatActivity() {

    companion object {
        const val TAG = "ReadingActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reading)
        val prdCode = intent.getStringExtra("prdCode") ?: ""
        val prdName = intent.getStringExtra("prdName") ?: ""
        findViewById<TextView>(R.id.tvParameter).text = "prdCode->$prdCode \n prdName->$prdName"
    }
}
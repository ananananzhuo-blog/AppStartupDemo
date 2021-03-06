package com.ananananzhuo.appstartupdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.startup.AppInitializer

fun logEE(msg:String){
    Log.e("公众号：安安安安卓",msg)
}
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppInitializer.getInstance(this)
            .initializeComponent(AndroidInitializer::class.java)

        logEE(Car.getInstance("小汽车：").toString())
        logEE(Car.getInstance("小汽车：").toString())
        logEE(Car.getInstance("小汽车：").toString())
    }
}
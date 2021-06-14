package com.ananananzhuo.appstartupdemo

import android.content.Context
import androidx.startup.AppInitializer
import androidx.startup.Initializer

/**
 * author  :mayong
 * function:
 * date    :2021/6/14
 **/
class AndroidInitializer : Initializer<Car> {
    override fun create(context: Context): Car {
        return Car.getInstance("出租车")
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}
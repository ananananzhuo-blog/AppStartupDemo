package com.ananananzhuo.appstartupdemo

import android.content.Context
import androidx.startup.Initializer

/**
 * author  :mayong
 * function:
 * date    :2021/6/14
 **/
class NoodleInitializer:Initializer<Noodle> {
    override fun create(context: Context): Noodle {
        return Noodle.getInstance()
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}
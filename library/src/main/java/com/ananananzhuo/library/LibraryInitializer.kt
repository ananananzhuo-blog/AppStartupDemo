package com.ananananzhuo.library

import android.content.Context
import androidx.startup.Initializer

/**
 * author  :mayong
 * function:
 * date    :2021/6/14
 **/
class LibraryInitializer:Initializer<Student> {
    override fun create(context: Context): Student {
        return Student.getInstance()
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
       return mutableListOf()
    }
}
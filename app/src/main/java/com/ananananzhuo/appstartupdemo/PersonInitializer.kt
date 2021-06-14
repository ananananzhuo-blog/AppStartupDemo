package com.ananananzhuo.appstartupdemo

import android.content.Context
import androidx.startup.Initializer

/**
 * author  :mayong
 * function:
 * date    :2021/6/14
 **/
class PersonInitializer : Initializer<Person> {
    override fun create(context: Context): Person {
        return Person.getInstance("李白").apply {
            addNoodle(Noodle.getInstance())
        }
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf(NoodleInitializer::class.java)
    }
}
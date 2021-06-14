package com.ananananzhuo.appstartupdemo

/**
 * author  :mayong
 * function:
 * date    :2021/6/14
 **/
class Person(val name:String) {
    private var noodle: Noodle? = null
    companion object {
        private var instance: Person? = null
        fun getInstance(name:String): Person {
            if (instance == null) {
                instance = Person(name)
            }
            return instance!!
        }
    }

    fun addNoodle(paramsnoodle: Noodle) {
        noodle = paramsnoodle
    }


    fun eat() {
        logEE("${name} 吃 ${noodle?.name}")
    }
}
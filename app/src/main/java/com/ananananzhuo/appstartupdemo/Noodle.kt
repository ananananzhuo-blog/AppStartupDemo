package com.ananananzhuo.appstartupdemo

/**
 * author  :mayong
 * function:
 * date    :2021/6/14
 **/
class Noodle {
    val name = "面条"

    companion object {
        private var instance: Noodle? = null
        fun getInstance(): Noodle {
            if (instance == null) {
                instance = Noodle()
            }
            return instance!!
        }
    }
}
package com.ananananzhuo.appstartupdemo

import java.util.*
import kotlin.random.Random

/**
 * author  :mayong
 * function:
 * date    :2021/6/14
 **/
class Car(private val name: String) {
    companion object {
        var instance: Car? = null
        fun getInstance(name: String): Car {
            if (instance == null) {
                instance = Car(name)
            }
            return instance!!
        }
    }

    override fun toString(): String {
        return "$name  ${Random.nextInt(100)}"
    }
}
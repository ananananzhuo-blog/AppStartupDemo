package com.ananananzhuo.library

import android.util.Log

/**
 * author  :mayong
 * function:
 * date    :2021/6/14
 **/
class Student(val name: String) {
    companion object {
        private val student = Student("安安安安卓")
        fun getInstance(): Student {
            return student
        }
    }

    fun study() {
        Log.e("tag", "${name}  好好学习")
    }
}
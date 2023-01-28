package com.example.guru16application.member

object datashare {


    var userid : String = ""

    fun setValue(string: String) {
        userid = string
    }

    fun getValue() : String{

        return userid

    }

    fun reValue() {
        userid = ""
    }

}
package com.example.guru16application.member

//모든 화면에서 공유할 login id
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
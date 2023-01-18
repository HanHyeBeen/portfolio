package com.example.guru16application.member

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.guru16application.R

class LoginActivity : AppCompatActivity() {
    lateinit var edit_id: EditText
    lateinit var edit_pw: EditText
    lateinit var btn_login: Button
    lateinit var btn_register: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_register.setOnClickListener {
            //회원가입 페이지로 이동
        }
    }
}
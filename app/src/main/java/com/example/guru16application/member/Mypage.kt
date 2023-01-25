package com.example.guru16application.member

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.guru16application.R

class Mypage : AppCompatActivity() {
    lateinit var btn_logout: Button
    lateinit var btn_delete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)

        btn_logout = findViewById(R.id.btn_logout)
        btn_delete = findViewById(R.id.btn_delete)
    }

}
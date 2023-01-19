package com.example.guru16application.member

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.guru16application.MainActivity
import com.example.guru16application.R

class LoginActivity : AppCompatActivity() {
    lateinit var dbManager: DBManager

    lateinit var edit_email: EditText
    lateinit var edit_pw: EditText
    lateinit var btn_login: Button
    lateinit var btn_register: Button

    val dialog = AlertDialog.Builder(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        edit_email = findViewById(R.id.edit_email)
        edit_pw = findViewById(R.id.edit_pw)

        btn_login.setOnClickListener {


            if(dbManager.getResult(edit_email.getText().toString(), edit_pw.getText().toString()) == true) {
                dialog.setMessage("로그인 성공")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            else{
                dialog.setMessage("로그인 실패")
            }
        }

        btn_register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }
}
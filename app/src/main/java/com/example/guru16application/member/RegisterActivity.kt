package com.example.guru16application.member

import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.guru16application.R

class RegisterActivity : AppCompatActivity() {
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var addName: EditText
    lateinit var addTel: EditText
    lateinit var addMail: EditText
    lateinit var addPw: EditText
    lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        addName = findViewById(R.id.addName)
        addTel = findViewById(R.id.addTel)
        addMail = findViewById(R.id.addMail)
        addPw = findViewById(R.id.addPw)
        btnRegister = findViewById(R.id.btnRegister)

        dbManager = DBManager(this, "personDB", null, 1)

        btnRegister.setOnClickListener {
            var str_name: String = addName.text.toString()
            var str_tel: String = addTel.text.toString()
            var str_mail: String = addMail.text.toString()
            var str_pw: String = addPw.text.toString()

            sqlitedb = dbManager.writableDatabase
            sqlitedb.execSQL("INSERT INTO person VALUES ('" + str_name + "'," + str_tel + "'," + str_mail + "'," + str_pw + "')")
            sqlitedb.close()
        }
    }
}
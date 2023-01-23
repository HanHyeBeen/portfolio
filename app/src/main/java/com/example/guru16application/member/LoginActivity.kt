package com.example.guru16application.member

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.example.guru16application.MainActivity
import com.example.guru16application.R

class LoginActivity : AppCompatActivity() {

    lateinit var dbManager: DBManager
    lateinit var  sqlDB : SQLiteDatabase

    lateinit var editID: EditText
    lateinit var editPW: EditText
    lateinit var btn_login: Button
    lateinit var btn_register: Button
    lateinit var autoLogin: CheckBox

    lateinit var str_name : String
    lateinit var str_id : String
    lateinit var str_pw : String

    //val dialog = AlertDialog.Builder(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editID = findViewById(R.id.editID)
        editPW = findViewById(R.id.editPW)
        btn_login = findViewById(R.id.btn_login)
        btn_register = findViewById(R.id.btn_register)
        autoLogin = findViewById(R.id.autoLogin)

        loadUser2()

        // ───────────────────────── 이벤트 정의 : 로그인 버튼 ─────────────────────────

        btn_login.setOnClickListener {

            var inputID = editID.text.toString()
            var inputPW = editPW.text.toString()

            dbManager = DBManager(this, "memberDB", null, 1)
            sqlDB = dbManager.readableDatabase

            var cursor : Cursor
            cursor = sqlDB.rawQuery("SELECT * FROM memberTBL WHERE id = '$inputID';", null)

            // 칼럼 제목 : name / tel / id / pw
            if(cursor != null && cursor.moveToFirst()) {

                str_name = cursor.getString(cursor.getColumnIndexOrThrow("name")).toString()
                str_id = cursor.getString(cursor.getColumnIndexOrThrow("id")).toString()
                str_pw = cursor.getString(cursor.getColumnIndexOrThrow("pw")).toString()

                cursor.close()
                sqlDB.close()
                dbManager.close()

                if(str_id == inputID)
                {
                    if(str_pw != inputPW)
                    {
                        Toast.makeText(applicationContext, "비밀번호가 다릅니다", Toast.LENGTH_SHORT).show()
                    }
                    else
                    {
                        if(autoLogin.isChecked()){
                            saveUser2(inputID, inputPW)
                        }

                        Toast.makeText(applicationContext, "$str_name 님 환영합니다", Toast.LENGTH_SHORT).show()

                        var intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
            else
            {
                Toast.makeText(applicationContext, "등록된 아이디가 없습니다", Toast.LENGTH_SHORT).show()
            }
        }

        // ───────────────────────── 이벤트 정의 : 가입 버튼 ─────────────────────────

        btn_register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    // ───────────────────────── 자동 로그인을 위한 메소드 ─────────────────────────

    private fun saveUser(id : String, pw : String) {
        var pref = this.getPreferences(0)
        var editor = pref.edit()

        editor.putString("KEY_ID", editID.text.toString()).apply()
        editor.putString("KEY_PW", editPW.text.toString()).apply()
    }

    private fun loadUser() {
        var pref = this.getPreferences(0)
        var id = pref.getString("KEY_ID", "")
        var pw = pref.getString("KEY_PW", "")

        if(id != "" && pw != "")
        {
            editID.setText(id.toString())
            editPW.setText(pw.toString())
        }
    }

    // ───────────────────────── 자동 로그인을 위한 메소드 ─────────────────────────

    private fun saveUser2(id : String, pw : String) {
        var pref = this.getSharedPreferences("autoLogin", Activity.MODE_PRIVATE)
        var editor = pref.edit()

        editor.putString("KEY_ID", editID.text.toString()).apply()
        editor.putString("KEY_PW", editPW.text.toString()).apply()
        editor.commit()
    }

    private fun loadUser2() {
        var pref = this.getSharedPreferences("autoLogin", Activity.MODE_PRIVATE)
        var id = pref.getString("KEY_ID", "")
        var pw = pref.getString("KEY_PW", "")

        if(id != "" && pw != "")
        {
            editID.setText(id.toString())
            editPW.setText(pw.toString())
        }
    }

    /*
    로그아웃 구현 시
    private fun clearUser2() {
        var pref = this.getSharedPreferences("autoLogin", Activity.MODE_PRIVATE)
        var editor = pref.edit()

        editor.clear()
        editor.commit()
    }
    * */
}
package com.example.guru16application.member

import android.app.AlertDialog
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.example.guru16application.MainActivity
import com.example.guru16application.R

class LoginActivity : AppCompatActivity() {

    val TAG: String = "LoginActivity"
    lateinit var dbManager: DBManager
    lateinit var  sqlDB : SQLiteDatabase

    lateinit var editID: EditText
    lateinit var editPW: EditText
    lateinit var btn_login: Button
    lateinit var btn_register: Button

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

        // ───────────────────────── 이벤트 정의 : 로그인 버튼 ─────────────────────────

        btn_login.setOnClickListener {

            var inputID = editID.text.toString()
            var inputPW = editPW.text.toString()

            dbManager = DBManager(this, "member", null, 1)
            sqlDB = dbManager.readableDatabase

            var cursor : Cursor
            cursor = sqlDB.rawQuery("SELECT * FROM member WHERE id = '$inputID';", null)

            // 칼럼 제목 : name / tel / id / pw
            if(cursor != null && cursor.moveToFirst()) {

                str_id = cursor.getString(cursor.getColumnIndexOrThrow("id")).toString()
                str_pw = cursor.getString(cursor.getColumnIndexOrThrow("pw")).toString()

                Log.d(TAG, "$str_id , $str_pw")

                cursor.close()
                sqlDB.close()
                dbManager.close()

                if(str_id == null)
                {
                    Log.d(TAG, "일치하는 아이디가 없습니다")
                }
                if(str_pw != inputPW)
                {
                    Log.d(TAG, "비밀번호가 틀립니다")
                }
                else
                {
                    Log.d(TAG, "로그인 성공")
                    val sharedPreference = getSharedPreferences("other", 0)
                    val editor = sharedPreference.edit()
                    editor.putString("id", inputID)
                    editor.putString("pw", inputPW)
                    editor.apply()

                    //myApplication.prefs.setString("id", id)
                    //myApplication.prefs.setString("pw", pw)

                    var intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }

            }


        }

        // ───────────────────────── 이벤트 정의 : 가입 버튼 ─────────────────────────

        btn_register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }
}
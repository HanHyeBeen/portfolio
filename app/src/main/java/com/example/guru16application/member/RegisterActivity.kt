package com.example.guru16application.member

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.guru16application.R

class RegisterActivity : AppCompatActivity() {
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var registerName: EditText
    lateinit var registerTel: EditText
    lateinit var registerID: EditText
    lateinit var registerPW: EditText
    lateinit var btnRegister: Button

    val TAG: String = "Register"
    var isExistBlank = false
    var isPWSame = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerName = findViewById(R.id.registerName)
        registerTel = findViewById(R.id.registerTel)
        registerID = findViewById(R.id.registerID)
        registerPW = findViewById(R.id.registerPW)
        btnRegister = findViewById(R.id.btnRegister)

        dbManager = DBManager(this, "member", null, 1)

        btnRegister.setOnClickListener {
            Log.d(TAG, "회원가입 버튼 클릭")

            val name = registerName.text.toString()
            val tel = registerTel.text.toString()
            val id = registerID.text.toString()
            val pw = registerPW.text.toString()

            do {
                if (name.isEmpty() || tel.isEmpty() || id.isEmpty() || pw.isEmpty()) {
                    isExistBlank = true
                }

                if (!isExistBlank) {
                    dialog("success")

                    // 입력한 정보 저장
                    var str_name: String = registerName.text.toString()
                    var str_tel: String = registerTel.text.toString()
                    var str_id: String = registerID.text.toString()
                    var str_pw: String = registerPW.text.toString()

                    sqlitedb = dbManager.writableDatabase
                    // 칼럼 제목 : name / tel / id / pw
                    sqlitedb.execSQL(
                        "INSERT INTO member VALUES ('"
                                + str_name + "','"
                                + str_tel + "','"
                                + str_id + "','"
                                + str_pw + "')"
                    )
                    sqlitedb.close()

                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    if (isExistBlank) {   // 작성 안한 항목이 있을 경우 다이얼로그
                        dialog("blank")
                    }
                }
            } while (isExistBlank)
        }
    }

    fun dialog(type: String){
        val dialog = AlertDialog.Builder(this)

        if(type == "blank"){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("입력란을 모두 작성해주세요")
        } else if(type == "success") {
            dialog.setTitle("회원가입 성공")
            dialog.setMessage("로그인 후 이용하세요")
        }

        val dialog_listener = object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which){
                    DialogInterface.BUTTON_POSITIVE ->
                        Log.d(TAG, "다이얼로그")
                }
            }
        }

        dialog.setPositiveButton("확인",dialog_listener)
        dialog.show()
    }

}
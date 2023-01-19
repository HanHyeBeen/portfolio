package com.example.guru16application.member

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.guru16application.R

class RegisterActivity : AppCompatActivity() {
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var addName: EditText
    lateinit var addTel: EditText
    lateinit var addMail: EditText
    lateinit var addPw: EditText
    lateinit var addPwCheck: EditText
    lateinit var btnRegister: Button

    val TAG: String = "Register"
    var isExistBlank = false
    var isPWSame = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        addName = findViewById(R.id.addName)
        addTel = findViewById(R.id.addTel)
        addMail = findViewById(R.id.addMail)
        addPw = findViewById(R.id.addPw)
        addPwCheck = findViewById(R.id.addPwCheck)
        btnRegister = findViewById(R.id.btnRegister)

        dbManager = DBManager(this, "member", null, 1)

        btnRegister.setOnClickListener {
            Log.d(TAG, "회원가입 버튼 클릭")

            val name = addName.text.toString()
            val tel = addTel.text.toString()
            val mail = addMail.text.toString()
            val pw = addPw.text.toString()
            val pw_re = addPwCheck.text.toString()

            if(name.isEmpty() || tel.isEmpty() || mail.isEmpty() || pw.isEmpty() || pw_re.isEmpty()){
                isExistBlank = true
            }
            else{
                if(pw == pw_re){
                    isPWSame = true
                }
            }

            if(!isExistBlank && isPWSame) {
                Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()

                // 입력한 정보 저장
                var str_name: String = addName.text.toString()
                var str_tel: String = addTel.text.toString()
                var str_mail: String = addMail.text.toString()
                var str_pw: String = addPw.text.toString()

                sqlitedb = dbManager.writableDatabase
                sqlitedb.execSQL("INSERT INTO member VALUES ('"
                        + str_name + "',"
                        + str_tel + "',"
                        + str_mail + "',"
                        + str_pw + "')")
                sqlitedb.close()

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            else{
                if(isExistBlank){   // 작성 안한 항목이 있을 경우
                    dialog("blank")
                }
                else if(!isPWSame){ // 입력한 비밀번호가 다를 경우
                    dialog("not same")
                }
            }
        }
    }
    // 회원가입 실패시
    fun dialog(type: String){
        val dialog = AlertDialog.Builder(this)

        if(type == "blank"){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("입력란을 모두 작성해주세요")
        }
        else if(type == "not same"){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("비밀번호가 다릅니다")
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
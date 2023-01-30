package com.example.guru16application.member

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.guru16application.MainActivity
import com.example.guru16application.R

class RegisterActivity : AppCompatActivity() {
    private lateinit var dbManager: DBManager
    private lateinit var sqlitedb: SQLiteDatabase

    private lateinit var registerName: EditText
    private lateinit var registerTel: EditText
    private lateinit var registerID: EditText
    private lateinit var registerPW: EditText
    private lateinit var btnRegister: Button

    val TAG: String = "Register"
    var isExistBlank = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerName = findViewById(R.id.registerName)
        registerTel = findViewById(R.id.registerTel)
        registerID = findViewById(R.id.registerID)
        registerPW = findViewById(R.id.registerPW)
        btnRegister = findViewById(R.id.btnRegister)



        dbManager = DBManager(this, "memberDB", null, 1)

        btnRegister.setOnClickListener {
            Log.d(TAG, "회원가입 버튼 클릭")

            isExistBlank = false

            val name = registerName.text.toString()
            val tel = registerTel.text.toString()
            val id = registerID.text.toString()
            val pw = registerPW.text.toString()

                if (name.isBlank() || tel.isBlank() || id.isBlank() || pw.isBlank()) {
                    isExistBlank = true
                }

            if (isExistBlank) {
                dialog("blank")

            }else {

                sqlitedb = dbManager.readableDatabase
                sqlitedb = dbManager.writableDatabase

                var checkCursor: Cursor
                checkCursor = sqlitedb.rawQuery("SELECT * FROM memberTBL WHERE id = '"+id+"';",null)

                if(checkCursor.count == 0) {
                    dialog("success")

                    // 입력한 정보 저장
                    var str_name: String = registerName.text.toString()
                    var str_tel: String = registerTel.text.toString()
                    var str_id: String = registerID.text.toString()
                    var str_pw: String = registerPW.text.toString()

                    // 칼럼 제목 : name / phone / id PRIMARY KEY/ pw
                    sqlitedb.execSQL(
                        "INSERT INTO memberTBL VALUES ('"
                                + str_name + "','"
                                + str_tel + "','"
                                + str_id + "','"
                                + str_pw + "')"
                    )
                    sqlitedb.close()
                    dbManager.close()

                } else {
                    dialog("sameid")
                }
            }


        }
    }

    fun dialog(type: String){
        val dialog = AlertDialog.Builder(this)

        val dialog_listener = DialogInterface.OnClickListener { dialog, which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE ->{
                    finishAffinity()
                    val intent : Intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }

                DialogInterface.BUTTON_NEGATIVE ->{

                }


            }
        }

        if(type == "blank"){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("입력란을 모두 작성해주세요")
            dialog.setNegativeButton("돌아가기",dialog_listener)
            dialog.show()
        } else if(type == "success") {
            dialog.setTitle("회원가입 성공")
            dialog.setMessage("로그인 후 이용하세요")
            dialog.setPositiveButton("확인",dialog_listener)
            dialog.setCancelable(false)
            dialog.show()
        } else if(type == "sameid"){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("같은 아이디가 존재합니다.")
            dialog.setNegativeButton("돌아가기",dialog_listener)
            dialog.show()
        }


    }

}
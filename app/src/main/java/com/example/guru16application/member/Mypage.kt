package com.example.guru16application.member

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.guru16application.MainActivity
import com.example.guru16application.R

class Mypage : AppCompatActivity() {
    lateinit var btn_logout: Button
    lateinit var btn_delete: Button
    lateinit var btn_back : ImageButton
    lateinit var userfix : Button

    lateinit var username : EditText
    lateinit var usertel : EditText
    lateinit var email : TextView


    // 뒤로가기 버튼 살리기

    lateinit var userid : datashare
    lateinit var dbManager: DBManager
    lateinit var sqlite : SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)

        btn_logout = findViewById(R.id.btn_logout)
        btn_delete = findViewById(R.id.btn_delete)
        btn_back = findViewById(R.id.mypage_back)
        username = findViewById(R.id.editname)
        usertel = findViewById(R.id.edittel)
        email = findViewById(R.id.email)
        userfix = findViewById(R.id.userfix)
        userid = datashare

        var id = datashare.getValue()

        dbManager = DBManager(this, "memberDB", null, 1)
        sqlite = dbManager.readableDatabase
        sqlite = dbManager.writableDatabase

        var cursor : Cursor
        cursor = sqlite.rawQuery("SELECT * FROM memberTBL WHERE id = '$id';", null)

        while (cursor.moveToNext()){
            var Userid = cursor.getString((cursor.getColumnIndexOrThrow("id"))).toString()
            var Username = cursor.getString((cursor.getColumnIndexOrThrow("name"))).toString()
            var Usertel = cursor.getString((cursor.getColumnIndexOrThrow("phone"))).toString()

            email.text = Userid + "@swu.ac.kr"
            username.setText(Username)
            usertel.setText(Usertel)
        }


        btn_logout.setOnClickListener {
            userid.reValue()
            val intent : Intent = Intent(this, LoginActivity::class.java)
            finishAffinity()
            startActivity(intent)
        }

        btn_back.setOnClickListener {
            finish()
        }

        btn_delete.setOnClickListener {
            var id = userid.getValue()
            userid.reValue()

            dbManager = DBManager(this, "memberDB", null, 1)
            sqlite = dbManager.readableDatabase
            sqlite = dbManager.writableDatabase
            sqlite.execSQL("DELETE FROM memberTBL WHERE id = '" + id + "';")

            val intent : Intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("del", "yes")
            finishAffinity()
            startActivity(intent)

        }

        userfix.setOnClickListener {
            var changeName = username.text.toString()
            var changePhone = usertel.text.toString()

            Toast.makeText(this, "변경되었습니다!", Toast.LENGTH_SHORT).show()



            if(changeName.isEmpty() || changePhone.isEmpty()){
                Toast.makeText(this, "제대로 입력하세요", Toast.LENGTH_SHORT).show()
            }else{

                dbManager = DBManager(this, "memberDB", null, 1)
                sqlite = dbManager.readableDatabase
                sqlite = dbManager.writableDatabase
                sqlite.execSQL("UPDATE memberTBL SET name = '"+changeName+"', phone = '"+changePhone +"' WHERE id = '" + id + "';")

                username.setText(changeName)
                usertel.setText(changePhone)

                /*finishAffinity()
                startActivity(Intent(this,MainActivity::class.java))*/
            }



        }
    }

}
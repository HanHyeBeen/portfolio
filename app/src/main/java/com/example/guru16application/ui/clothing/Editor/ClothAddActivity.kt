package com.example.guru16application.ui.clothing.Editor

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteStatement
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.guru16application.R
import com.example.guru16application.member.datashare
import com.example.guru16application.ui.ProductDBHelper
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*


class ClothAddActivity : AppCompatActivity() {


    lateinit var img : ImageView
    lateinit var imgadd1 : ImageView
    lateinit var imgadd2 : ImageView
    lateinit var imgadd3 : ImageView

    //날짜 설정
    lateinit var startCbtn  : ImageButton
    lateinit var startY : TextView
    lateinit var startM : TextView
    lateinit var startD : TextView

    lateinit var finCbtn  : ImageButton
    lateinit var finY : TextView
    lateinit var finM : TextView
    lateinit var finD : TextView

    //등록하기
    lateinit var save : Button
    lateinit var back : ImageButton

    //데이터 받기
    lateinit var incName : EditText
    lateinit var category: RadioGroup
    lateinit var school: RadioButton
    lateinit var grade: RadioButton
    lateinit var url : EditText
    lateinit var more_c : EditText

    //db 관련 변수
    lateinit var dbManager: ProductDBHelper
    lateinit var sqlites : SQLiteDatabase

    //id
    lateinit var userid : datashare

    private val open_gall = 1

    var index : Int = 0
    var start : Int = 0
    var fin : Int = 0

    var imgArr : Array<Bitmap?> = arrayOfNulls<Bitmap?>(4)
    lateinit var bytearr : ByteArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cloth_add)

        userid = datashare
        //등록하기 버튼
        back = findViewById(R.id.back)
        save = findViewById(R.id.save)
        incName = findViewById(R.id.incName)
        category = findViewById(R.id.category)
        school = findViewById(R.id.shcool)
        grade = findViewById(R.id.grade)
        url = findViewById(R.id.incurl)
        more_c = findViewById(R.id.more_c)

        //이미지 등록
        img = findViewById(R.id.mainImage)
        imgadd1 = findViewById(R.id.add1)
        imgadd2 = findViewById(R.id.add2)
        imgadd3 = findViewById(R.id.add3)

        //fin 날짜 입력
        finCbtn = findViewById(R.id.finCbtn)
        finY = findViewById(R.id.finY)
        finM = findViewById(R.id.finM)
        finD = findViewById(R.id.finD)

        //start n 날짜 입력
        startCbtn = findViewById(R.id.startCbtn)
        startY = findViewById(R.id.startY)
        startM = findViewById(R.id.startM)
        startD = findViewById(R.id.startD)

        //db 관련

        dbManager = ProductDBHelper(this, "food.db")

        back.setOnClickListener {
            finish()
        }

        //등록하기 버튼 클릭 시

        save.setOnClickListener {

            var pass = 0
            var i = 0

            val name = incName.text.toString()
            var type_c : Int = 0
            var startY_s = startY.text.toString().toIntOrNull()
            var startM_s = startM.text.toString().toIntOrNull()
            var startD_s = startD.text.toString().toIntOrNull()
            var finY_s = finY.text.toString().toIntOrNull()
            var finM_s = finM.text.toString().toIntOrNull()
            var finD_s = finD.text.toString().toIntOrNull()
            var url_s = url.text.toString()
            var more = more_c.text.toString()
            var id = userid.getValue()


            if(category.checkedRadioButtonId == R.id.shcool){
                type_c = 1
            }
            if(category.checkedRadioButtonId == R.id.grade){
                type_c = 2
            }

            sqlites = dbManager.readableDatabase
            sqlites = dbManager.writableDatabase

            var cursor : Cursor
            cursor = sqlites.rawQuery("SELECT * FROM cloth WHERE cName = '"+name+"';",null)

            if(cursor.count !=0 ){
                pass = 1
                Toast.makeText(this, "중복되지 않은 제목을 입력하세요", Toast.LENGTH_SHORT).show()
            }else {

                if (startY_s == null || startM_s == null || startD_s == null || finY_s == null || finM_s == null || finD_s == null) {
                    pass = 1
                    Toast.makeText(this, "날짜를 입력하세요", Toast.LENGTH_SHORT).show()
                } else {
                    if(imgArr[0]==null){
                        pass = 1
                        Toast.makeText(this, "메인 이미지를 등록하세요", Toast.LENGTH_SHORT).show()
                    }else{
                        bytearr = byteTobit(imgArr[0])
                        if(url_s.indexOf("https://") == -1){
                            pass = 1
                            Toast.makeText(this, "주소가 잘못되었습니다.", Toast.LENGTH_SHORT).show()
                        }else{
                            if(type_c == 0){
                                pass = 1
                                Toast.makeText(this, "종류를 선택하세요.", Toast.LENGTH_SHORT).show()
                            }
                        }

                    }

                }
            }

            if(pass == 1){
                Toast.makeText(this, "등록 실패!", Toast.LENGTH_SHORT).show()
            }else{
                if(fin - start <= 0){
                    Toast.makeText(this, "기간을 다시 설정해 주세요.", Toast.LENGTH_SHORT).show()
                }else{

                    var bytearray : ByteArray = byteTobit(imgArr[0])
                    var q : SQLiteStatement = sqlites.compileStatement("INSERT INTO cloth VALUES ('"+name+"','"+startY_s+"','"+startM_s+"','"+startD_s+"','"
                            +finY_s+"','"+finM_s+"','"+finD_s+"','"+id+"','"+more+"','"+url_s+"', ?, '"+type_c+"');")

                    q.bindBlob(1, bytearray)
                    q.execute()

                    while (i < 4){

                        if(imgArr[i] != null){

                            bytearr = byteTobit(imgArr[i])
                            var imgadd : SQLiteStatement = sqlites.compileStatement("INSERT INTO clothimg VALUES ('"+name+"',? );")
                            imgadd.bindBlob(1, bytearr)
                            imgadd.execute()

                        }
                        i++

                    }
                    Toast.makeText(this, "등록 완료!", Toast.LENGTH_SHORT).show()
                    downKeyboard()
                    finish()


                }
            }

        }

        more_c.setOnTouchListener { view, event ->
            if (view.id == R.id.more_c) {
                view.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> view.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        }





        img.setOnClickListener {
            index = 0
            openGallery()
        }
        imgadd1.setOnClickListener {
            index = 1
            openGallery()
        }
        imgadd2.setOnClickListener {
            index = 2
            openGallery()
        }
        imgadd3.setOnClickListener {
            index = 3
            openGallery()
        }

        //날짜 설정
        startCbtn.setOnClickListener {
            val cal = Calendar.getInstance()
            val dateListen = DatePickerDialog.OnDateSetListener { view, year, month, day ->
                startY.text = "$year"
                startM.text = "${month+1}"
                startD.text = "$day"

                start = year * 10000 + (month+1) * 100 + day
            }
            DatePickerDialog(this,dateListen,cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(
                Calendar.DAY_OF_MONTH)).show()
        }

        finCbtn.setOnClickListener {
            val cal = Calendar.getInstance()
            val dateListen = DatePickerDialog.OnDateSetListener { view, year, month, day ->
                finY.text = "$year"
                finM.text = "${month+1}"
                finD.text = "$day"

                fin = year * 10000 + (month+1) * 100 + day
            }
            DatePickerDialog(this,dateListen,cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(
                Calendar.DAY_OF_MONTH)).show()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            1 -> if (resultCode == RESULT_OK){
                val uri : Uri? = data?.data
                var bitmap :Bitmap? = null

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                }catch (e: IOException){
                    e.printStackTrace()
                }

                if (index == 0){
                    img.setImageBitmap(bitmap)
                    imgArr[0] = bitmap
                }
                if (index == 1){
                    imgadd1.setImageBitmap(bitmap)
                    imgArr[1] = bitmap
                }
                if (index == 2){
                    imgadd2.setImageBitmap(bitmap)
                    imgArr[2] = bitmap
                }
                if (index == 3){
                    imgadd3.setImageBitmap(bitmap)
                    imgArr[3] = bitmap
                }

            }
        }

    }

    private fun openGallery() {

        val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent, open_gall)
    }

    fun byteTobit(bitmap: Bitmap?): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap!!.compress(Bitmap.CompressFormat.PNG, 40, stream)
        return stream.toByteArray()

    }

    private fun downKeyboard() {
        if (this != null && this.currentFocus != null) {
            val inputManager: InputMethodManager =
                this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                this.currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}
package com.example.guru16application.ui.clothing

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ExpandableListView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.guru16application.R
import com.example.guru16application.ui.ImgShowActivity
import com.example.guru16application.ui.ProductDBHelper
import com.example.guru16application.ui.clothing.ReViewItem
import java.io.ByteArrayOutputStream


class ClothNextActivity : AppCompatActivity() {

    lateinit var backbtn : ImageButton
    lateinit var vlayout : LinearLayout
    lateinit var conlayout : ConstraintLayout
    lateinit var imgview : ImageView

    lateinit var mainImg : ImageView

    lateinit var imgBtn_L : ImageButton
    lateinit var imgBtn_R : ImageButton

    lateinit var dbManager : ProductDBHelper
    lateinit var sqlitedb : SQLiteDatabase
    lateinit var Image : ByteArray

    var imgArray = ArrayList<Bitmap>()

    lateinit var next_name : TextView
    lateinit var next_date : TextView
    lateinit var moretext : TextView
    lateinit var url_btn  :Button

    var url: String = ""




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clothnext)

        backbtn = findViewById(R.id.cloth_back)
        conlayout = findViewById(R.id.more_con)
        vlayout = findViewById(R.id.layout_expand)
        imgview = findViewById(R.id.text_more)
        imgBtn_L = findViewById(R.id.leftButton)
        imgBtn_R = findViewById(R.id.rightButton)
        mainImg = findViewById(R.id.cloth_img)

        next_name = findViewById(R.id.next_name)
        next_date = findViewById(R.id.next_date)
        moretext = findViewById(R.id.longtext)
        url_btn = findViewById(R.id.Url_btn)

        backbtn.setOnClickListener {
            finish()
        }

        //상세보기
        conlayout.setOnClickListener{
            if(vlayout.visibility == View.VISIBLE){

                imgview.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24)
                vlayout.visibility = View.GONE


            }else{

                imgview.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24)
                vlayout.visibility = View.VISIBLE

            }

        }

        var nextext = intent.getStringExtra("searchName")



        //검색한 거 적용하기

        dbManager = ProductDBHelper(this, "food.db")

        sqlitedb = dbManager.readableDatabase
        sqlitedb = dbManager.writableDatabase


        var cursor: Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM clothimg WHERE ciName = '" +nextext+ "' ;", null)

        var cursor_search_c: Cursor
        cursor_search_c = sqlitedb.rawQuery("SELECT * FROM cloth WHERE cName = '" +nextext+ "' ;", null)

        if(cursor_search_c.moveToNext()) {
            var startY = cursor_search_c.getInt(cursor_search_c.getColumnIndexOrThrow("cStartY"))
            var startM = cursor_search_c.getInt(cursor_search_c.getColumnIndexOrThrow("cStartM"))
            var startD = cursor_search_c.getInt(cursor_search_c.getColumnIndexOrThrow("cStartD"))
            var finY = cursor_search_c.getInt(cursor_search_c.getColumnIndexOrThrow("cFinY"))
            var finM = cursor_search_c.getInt(cursor_search_c.getColumnIndexOrThrow("cFinM"))
            var finD = cursor_search_c.getInt(cursor_search_c.getColumnIndexOrThrow("cFinD"))
            var more = cursor_search_c.getString((cursor_search_c.getColumnIndexOrThrow("cMore"))).toString()
            url = cursor_search_c.getString((cursor_search_c.getColumnIndexOrThrow("cUrl"))).toString()

            next_name.text = nextext
            next_date.text = "$startY-$startM-$startD ~ $finY-$finM-$finD"

            moretext.text = more

        }





        //이미지 뷰 불러오기
        while (cursor.moveToNext()) {
            Image = cursor.getBlob(cursor.getColumnIndexOrThrow("ciImg"))
            val bitmap: Bitmap = BitmapFactory.decodeByteArray(Image, 0, Image.size)

            imgArray.add(bitmap)

        }


        mainImg.setImageBitmap(imgArray[0])

        var size : Int = imgArray.size

        var cursor_img : Int = 0

        imgBtn_R.setOnClickListener{

            cursor_img++
            if(cursor_img > size - 1){
                cursor_img = 0
            }
            mainImg.setImageBitmap(imgArray[cursor_img])



        }

        imgBtn_L.setOnClickListener{

            cursor_img--
            if(cursor_img < 0){
                cursor_img = size -1
            }
            mainImg.setImageBitmap(imgArray[cursor_img])

        }

        url_btn.setOnClickListener {
            val intent : Intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        mainImg.setOnClickListener {

            val intent: Intent = Intent(this, ImgShowActivity::class.java)
            intent.putExtra("name", nextext)
            intent.putExtra("index", cursor_img)
            startActivity(intent)


        }



    }

}
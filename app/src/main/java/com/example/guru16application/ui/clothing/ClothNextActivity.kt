package com.example.guru16application.ui.clothing

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ExpandableListView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.guru16application.R
import com.example.guru16application.ui.ProductDBHelper
import com.example.guru16application.ui.clothing.ReViewItem


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

        backbtn.setOnClickListener {
            finish()
        }

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

        Toast.makeText(this,"결과 : $nextext",Toast.LENGTH_SHORT).show()

        //검색한 거 적용하기

        dbManager = ProductDBHelper(this, "food.db")

        sqlitedb = dbManager.readableDatabase
        sqlitedb = dbManager.writableDatabase


        var cursor: Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM clothimg WHERE ciName = '" +nextext+ "' ;", null)



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











    }
}
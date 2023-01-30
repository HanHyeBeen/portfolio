package com.example.guru16application.ui

import android.content.res.Resources
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast

import com.example.guru16application.R
import com.github.chrisbanes.photoview.PhotoView

//이미지 크게보기
class ImgShowActivity : AppCompatActivity() {

    lateinit var backBtn : ImageButton
    lateinit var bigImg :PhotoView

    lateinit var dbManager : ProductDBHelper
    lateinit var sqlitedb : SQLiteDatabase

    lateinit var Image : ByteArray

    var imgArray = ArrayList<Bitmap>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_img_show)

        backBtn = findViewById(R.id.Bigshow_back)
        bigImg = findViewById(R.id.bigImg)

        var Bname = intent.getStringExtra("name")
        var Bindex = intent.getIntExtra("index" , 0)


        backBtn.setOnClickListener {
            finish()
        }

        dbManager = ProductDBHelper(this, "food.db")

        sqlitedb = dbManager.readableDatabase
        sqlitedb = dbManager.writableDatabase


        var cursor: Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM clothimg WHERE ciName = '" +Bname+ "' ;", null)

        while (cursor.moveToNext()) {
            Image = cursor.getBlob(cursor.getColumnIndexOrThrow("ciImg"))
            val bitmap: Bitmap = BitmapFactory.decodeByteArray(Image, 0, Image.size)

            imgArray.add(bitmap)

        }

       bigImg.setImageBitmap(imgArray[Bindex])








    }
}
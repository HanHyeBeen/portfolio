package com.example.guru16application.ui.food

import android.content.Context
import android.content.DialogInterface
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import com.example.guru16application.R
import com.example.guru16application.member.datashare
import com.example.guru16application.ui.ProductDBHelper

class FoodNextActivity : AppCompatActivity() {

    lateinit var dbManager: ProductDBHelper
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var Image : ByteArray

    lateinit var fimg : ImageView
    lateinit var fname : TextView
    lateinit var floc  : TextView
    lateinit var ftiem : TextView

    lateinit var fMenuEx : ListView
    lateinit var fComment : ListView

    lateinit var foodMenubar  : ConstraintLayout
    lateinit var morebtn  : ImageView
    lateinit var fmoreEx : LinearLayout

    lateinit var scroll : NestedScrollView

    lateinit var comentEdt  : EditText
    lateinit var comentplus  : ImageButton
    lateinit var foodback : ImageButton


    var mlist = arrayListOf<fMListItem>()

    var laststring = arrayListOf<String>()

    //id
    lateinit var shareUserid : datashare


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_next)

        shareUserid = datashare
        val id = shareUserid.getValue()

        var str_text = intent.getStringExtra("searchfName")

        fimg = findViewById(R.id.foodNextImg)
        fname = findViewById(R.id.foodNextName)
        floc = findViewById(R.id.foodNextLoc)
        ftiem = findViewById(R.id.foodNextTime)
        fMenuEx = findViewById(R.id.foodMenuText)
        morebtn = findViewById(R.id.food_more)
        fmoreEx = findViewById(R.id.foodMenuEx)
        foodMenubar = findViewById(R.id.foodMenu)
        fComment = findViewById(R.id.foodComment)
        scroll = findViewById(R.id.nestScroll)
        comentEdt = findViewById(R.id.foodCommentEdt)
        comentplus = findViewById(R.id.complus)
        foodback = findViewById(R.id.food_back)


        dbManager = ProductDBHelper(this, "food.db")
        sqlitedb = dbManager.readableDatabase
        sqlitedb = dbManager.writableDatabase


        var cursor: Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM food WHERE fName = '" + str_text + "';", null)


        while (cursor.moveToNext()) {
            Image = cursor.getBlob(cursor.getColumnIndexOrThrow("fimg"))
            val bitmap: Bitmap = BitmapFactory.decodeByteArray(Image, 0, Image.size)
            var Name = cursor.getString((cursor.getColumnIndexOrThrow("fName"))).toString()
            var Loc = cursor.getString((cursor.getColumnIndexOrThrow("fLoc"))).toString()
            var startT = cursor.getInt((cursor.getColumnIndexOrThrow("fstartTime")))
            var startM = cursor.getInt((cursor.getColumnIndexOrThrow("fstartmin")))
            var finT = cursor.getInt((cursor.getColumnIndexOrThrow("ffinTime")))
            var finM = cursor.getInt((cursor.getColumnIndexOrThrow("ffinmin")))

            var Time = "시작 시간 : ${startT} 시 ${startM} 분\n종료 시간 : ${finT} 시 ${finM} 분"

            fimg.setImageBitmap(bitmap)
            fname.text = Name
            floc.text = Loc
            ftiem.text = Time

        }

        var cursor1: Cursor
        cursor1 = sqlitedb.rawQuery("SELECT * FROM food_menu WHERE fName = '" + str_text + "';", null)

        while (cursor1.moveToNext()) {

            var Menu = cursor1.getString((cursor1.getColumnIndexOrThrow("fMenu"))).toString()
            var price = cursor1.getInt((cursor1.getColumnIndexOrThrow("fPri")))

            mlist.add(fMListItem(Menu,price.toString()))

        }


        val adapter = fMListAdapter(this, mlist)
        fMenuEx.adapter = adapter




        var cursor2: Cursor
        cursor2 = sqlitedb.rawQuery("SELECT * FROM food_comment WHERE fName = '" + str_text + "';", null)

        var slist = arrayListOf<String>()

        while (cursor2.moveToNext()) {

            var Comet = cursor2.getString((cursor2.getColumnIndexOrThrow("fcomment"))).toString()

            slist.add(Comet)
        }

        val adapter2 = fCListAdapter(this, slist)
        laststring = slist

        fComment.adapter = adapter2



        comentplus.setOnClickListener {

            downKeyboard()

            var comment : String = comentEdt.text.toString()

            comentEdt.text = null

            sqlitedb = dbManager.readableDatabase
            sqlitedb = dbManager.writableDatabase



            if(comment == ""){
                Toast.makeText(this, "입력한 내용이 없습니다.", Toast.LENGTH_SHORT).show()
            }else{

                sqlitedb.execSQL("INSERT INTO food_comment VALUES ('"+ str_text +"', '"+ comment +"','"+ id +"' );")

                var cursor3: Cursor
                cursor3 = sqlitedb.rawQuery("SELECT * FROM food_comment WHERE fName = '" + str_text + "';", null)

                var list_i = arrayListOf<String>()

                while (cursor3.moveToNext()) {

                    var Comet = cursor3.getString((cursor3.getColumnIndexOrThrow("fcomment"))).toString()

                    list_i.add(Comet)
                }

                laststring = list_i

                val adapter3 = fCListAdapter(this, list_i)
                adapter3.notifyDataSetChanged()
                fComment.adapter = adapter3
            }


        }



        foodMenubar.setOnClickListener {
            if(fmoreEx.visibility == View.VISIBLE){

                morebtn.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24)
                fmoreEx.visibility = View.GONE


            }else{

                morebtn.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24)
                fmoreEx.visibility = View.VISIBLE

            }

        }

        fComment.setOnItemClickListener { adapterView, view, i, l ->
            var item: String = laststring[i]

            val builder = AlertDialog.Builder(this)
            builder.setTitle("삭제하시겠습니까?")
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener{ dialog, i ->
                        val sqlitdb = dbManager.readableDatabase

                        var cursor_check : Cursor
                        cursor_check = sqlitedb.rawQuery( "SELECT * FROM food_comment WHERE fName = '" + str_text + "' AND fcomment = '" + item + "' AND fuser = '"+id+"';", null)
                        if(cursor_check.count == 0){
                            Toast.makeText(this, "본인이 쓴 글이 아닙니다.", Toast.LENGTH_SHORT).show()
                        }else {

                            sqlitdb.execSQL("DELETE FROM food_comment WHERE fcomment = '" + item + "' AND fuser = '" + id + "';")
                            var cursor4: Cursor
                            cursor4 = sqlitedb.rawQuery(
                                "SELECT * FROM food_comment WHERE fName = '" + str_text + "';",
                                null
                            )

                            var list_i = arrayListOf<String>()

                            while (cursor4.moveToNext()) {

                                var Comet =
                                    cursor4.getString((cursor4.getColumnIndexOrThrow("fcomment")))
                                        .toString()

                                list_i.add(Comet)
                            }

                            laststring = list_i

                            val adapter3 = fCListAdapter(this, list_i)
                            adapter3.notifyDataSetChanged()
                            fComment.adapter = adapter3
                        }

                    })
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener{ dialog, i ->

                    })
            builder.show()

        }

        foodback.setOnClickListener {
            finish()
        }

        fMenuEx.setOnTouchListener(View.OnTouchListener { v, event ->
            scroll.requestDisallowInterceptTouchEvent(true)
            false
        })

        fComment.setOnTouchListener(View.OnTouchListener { v, event ->
            scroll.requestDisallowInterceptTouchEvent(true)
            false
        })



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
package com.example.guru16application.ui.food

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.guru16application.MainActivity
import com.example.guru16application.databinding.FragmentFoodBinding
import com.example.guru16application.databinding.FragmentFoodBinding.*
import com.example.guru16application.ui.ProductDBHelper
import com.sothree.slidinguppanel.SlidingUpPanelLayout

class FoodFragment : Fragment() {

    private var _binding: FragmentFoodBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    lateinit var mainActivity: MainActivity


    override fun onAttach(context: Context) {
        super.onAttach(context)


        mainActivity = context as MainActivity
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(FoodViewModel::class.java)

        _binding = inflate(inflater, container, false)
        val root: View = binding.root

        //이후 삭제해도 괜찮은 코드
        /*val textView: TextView = binding.textFood
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/

        lateinit var dbManager: ProductDBHelper
        lateinit var sqlitedb: SQLiteDatabase
        lateinit var Image: ByteArray

        var list = arrayListOf<ListViewItem>()  //data 받아올 장소

        //val view: View = inflater.inflate(R.layout.fragment_food, container, false)


        //db 관련 코드 : 데이터가 있는 db 접속하기
        dbManager = ProductDBHelper(mainActivity,"food.db")
        sqlitedb = dbManager.readableDatabase
        sqlitedb = dbManager.writableDatabase

        var cursor: Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM food ;", null)

        while(cursor.moveToNext()){
            Image = cursor.getBlob(cursor.getColumnIndexOrThrow("fimg"))
            val bitmap: Bitmap = BitmapFactory.decodeByteArray(Image,0,Image.size)
            var Name = cursor.getString((cursor.getColumnIndexOrThrow("fName"))).toString()
            var Menu = cursor.getString((cursor.getColumnIndexOrThrow("fLoc"))).toString()
            list.add(ListViewItem(bitmap,Name,Menu))

        }

        val listView1: ListView = binding.listView
        notificationsViewModel.list_n.observe(viewLifecycleOwner) {

            val modelist : ArrayList<ListViewItem> = list
            val Adapter_1 = ListViewAdapter(mainActivity, modelist)
            listView1.adapter = Adapter_1
        }
        sqlitedb.close()


        binding.foodSearchBtn.setOnClickListener {

            list.clear()
            //Toast.makeText(mainActivity,"됨", Toast.LENGTH_SHORT).show()

            var str_text: String = binding.foodSearchEdt.text.toString()

            sqlitedb = dbManager.readableDatabase
            sqlitedb = dbManager.writableDatabase

            if (str_text != "") {
                cursor =
                    sqlitedb.rawQuery("SELECT * FROM food WHERE fName LIKE '%" + str_text + "%';", null)
            } else {
                cursor = sqlitedb.rawQuery("SELECT * FROM food;", null)
            }


            while(cursor.moveToNext()) {
                Image = cursor.getBlob(cursor.getColumnIndexOrThrow("fimg"))
                val bitmap: Bitmap = BitmapFactory.decodeByteArray(Image, 0, Image.size)
                var Name = cursor.getString((cursor.getColumnIndexOrThrow("fName"))).toString()
                var Menu = cursor.getString((cursor.getColumnIndexOrThrow("fLoc"))).toString()
                list.add(ListViewItem(bitmap, Name, Menu))
            }

            val listView2: ListView = binding.listView
        notificationsViewModel.list_n.observe(viewLifecycleOwner) {

            val modelist : ArrayList<ListViewItem> = list
            val Adapter_1 = ListViewAdapter(mainActivity, modelist)
            listView2.adapter = Adapter_1
        }

            sqlitedb.close()



            //기존 데이터를 없애는 코드
            val new: EditText = binding.foodSearchEdt
            notificationsViewModel.text.observe(viewLifecycleOwner) {
                new.text = null
            }

            binding.bottomFoodPanel.panelState = SlidingUpPanelLayout.PanelState.EXPANDED

        }



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

/* MainActivity: asset에 있는 db 파일 읽어서 추가하고 접근하기
//create 추가
 var check: File = File(filePath + "food.db")
        if (check.exists()) {
            Toast.makeText(this, "있음", Toast.LENGTH_SHORT).show()
        } else {

            setDB(this)
            val mHelper: ProductDBHelper = ProductDBHelper(this, "food.db")
            db = mHelper.writableDatabase

        }

//변수
lateinit var db: SQLiteDatabase
var filePath: String = "/data/data/com.example.guru16application/databases/"

//함수
 private fun setDB(ctx: Context) {

        var folder: File = File(filePath)
        if (folder.exists()) {
        } else {
            folder.mkdirs();
        }
        var assetManager: AssetManager = ctx.resources.assets
        var outfile: File = File(filePath + "food.db")
        var IStr: InputStream? = null
        var fo: FileOutputStream? = null
        var filesize: Int = 0
        try {
            IStr = assetManager.open("food.db", AssetManager.ACCESS_BUFFER)
            filesize = IStr.available()
            if (outfile.length() <= 0) {
                val buffer = ByteArray(filesize)
                // byte[] tempdata = new byte[(int) filesize];
                IStr.read(buffer)
                IStr.close()
                outfile.createNewFile()
                fo = FileOutputStream(outfile)
                fo.write(buffer)
                fo.close()
            } else {
            }
        } finally {
        }
    }
 */
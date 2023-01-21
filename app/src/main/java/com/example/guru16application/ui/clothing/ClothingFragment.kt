package com.example.guru16application.ui.clothing

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.guru16application.MainActivity
import com.example.guru16application.ui.ProductDBHelper
import com.example.guru16application.databinding.FragmentClothingBinding



class ClothingFragment : Fragment() {

    private var _binding: FragmentClothingBinding? = null

    private val binding get() = _binding!!

    lateinit var Image: ByteArray

    var Relist = ArrayList<ReViewItem>()
    var Grlist = ArrayList<ReViewItem>()


    lateinit var mainActivity: MainActivity

    lateinit var dbManager: ProductDBHelper
    lateinit var sqlitedb: SQLiteDatabase


    override fun onAttach(context: Context) {
        super.onAttach(context)


        mainActivity = context as MainActivity
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val clothingViewModel = ViewModelProvider(this).get(ClothingViewModel::class.java)

        _binding = FragmentClothingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // 보일 아이템 검색
        dbManager = ProductDBHelper(mainActivity, "food.db")

        sqlitedb = dbManager.readableDatabase
        sqlitedb = dbManager.writableDatabase

        Relist.clear()
        Grlist.clear()

        var cursor: Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM food ;", null)



        while (cursor.moveToNext()) {
            Image = cursor.getBlob(cursor.getColumnIndexOrThrow("fimage"))
            val bitmap: Bitmap = BitmapFactory.decodeByteArray(Image, 0, Image.size)
            var Name = cursor.getString((cursor.getColumnIndexOrThrow("fName"))).toString()

            Relist.add(ReViewItem(bitmap, Name))
            Grlist.add(ReViewItem(bitmap, Name))

        }

        val recyler: RecyclerView = binding.recyclerHorizon
        val gridcloth: GridView = binding.clothGrid
        clothingViewModel.clothlist.observe(viewLifecycleOwner) {

            val relist: ArrayList<ReViewItem> = Relist
            val Adapter_2 = ReViewAdapter(mainActivity, relist)
            recyler.adapter = Adapter_2
        }

        clothingViewModel.clothlist.observe(viewLifecycleOwner) {

            val glist: ArrayList<ReViewItem> = Grlist
            val Adapter_3 = GriViewAdapter(mainActivity, glist)
            gridcloth.adapter = Adapter_3
        }


        sqlitedb.close()

        var expandSpec =
            View.MeasureSpec.makeMeasureSpec(Int.MAX_VALUE shr 2, View.MeasureSpec.AT_MOST)
        binding.clothGrid.measure(0, expandSpec)
        binding.clothGrid.getLayoutParams().height = check("SELECT * FROM food ;")


        //버튼 클릭 시 이벤트
        binding.clothSearchBtn.setOnClickListener {

            Relist.clear()
            Grlist.clear()
            downKeyboard()

            var cloth_text: String = binding.clothSearchEdt.text.toString()

            sqlitedb = dbManager.readableDatabase
            sqlitedb = dbManager.writableDatabase


            var cursor: Cursor
            cursor = sqlitedb.rawQuery(
                "SELECT * FROM food WHERE fName LIKE '%" + cloth_text + "%';",
                null
            )
            //cursor = sqlitedb.rawQuery("SELECT * FROM food ;", null)

            while (cursor.moveToNext()) {
                Image = cursor.getBlob(cursor.getColumnIndexOrThrow("fimage"))
                val bitmap: Bitmap = BitmapFactory.decodeByteArray(Image, 0, Image.size)
                var Name = cursor.getString((cursor.getColumnIndexOrThrow("fName"))).toString()

                Relist.add(ReViewItem(bitmap, Name))
                Grlist.add(ReViewItem(bitmap, Name))
            }

            clothingViewModel.clothlist.observe(viewLifecycleOwner) {

                val relist_c: ArrayList<ReViewItem> = Relist
                val Adapter_2 = ReViewAdapter(mainActivity, relist_c)
                recyler.adapter = Adapter_2
            }

            val gridcloth2: GridView = binding.clothGrid

            clothingViewModel.clothlist.observe(viewLifecycleOwner) {

                val glist_c: ArrayList<ReViewItem> = Grlist
                val Adapter_3 = GriViewAdapter(mainActivity, glist_c)
                gridcloth2.adapter = Adapter_3
            }

            var number: Int = Grlist.size

            sqlitedb.close()

            var sqlsen: String = "SELECT * FROM food WHERE fName LIKE '%$cloth_text%';"


            //사이즈 조절
            var expandSpec =
                View.MeasureSpec.makeMeasureSpec(Int.MAX_VALUE shr 2, View.MeasureSpec.AT_MOST)
            binding.clothGrid.measure(0, expandSpec)
            check(sqlsen)
            binding.clothGrid.getLayoutParams().height = check(sqlsen)

        }

        // 그리드 레이아웃 클릭 시 이벤트
        binding.clothGrid.setOnItemClickListener { adapterView, view, i, l ->
            var item:ReViewItem = Grlist[i]
            val test = item.name
            Toast.makeText(activity, "$test", Toast.LENGTH_SHORT).show()
            //액티비티와 프래그먼트 연결
            /*var intent: Intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)*/

        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun downKeyboard() {
        if (activity != null && requireActivity().currentFocus != null) {
            val inputManager: InputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                requireActivity().currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    private fun check(string: String): Int {

        Grlist.clear()

        dbManager = ProductDBHelper(mainActivity, "food.db")

        sqlitedb = dbManager.readableDatabase
        sqlitedb = dbManager.writableDatabase

        var cursor: Cursor
        cursor = sqlitedb.rawQuery(string, null)



        while (cursor.moveToNext()) {
            Image = cursor.getBlob(cursor.getColumnIndexOrThrow("fimage"))
            val bitmap: Bitmap = BitmapFactory.decodeByteArray(Image, 0, Image.size)
            var Name = cursor.getString((cursor.getColumnIndexOrThrow("fName"))).toString()

            Grlist.add(ReViewItem(bitmap, Name))

        }


        val gridcloth: GridView = binding.clothGrid
        val glist: ArrayList<ReViewItem> = Grlist
        val Adapter_3 = GriViewAdapter(mainActivity, glist)
        gridcloth.adapter = Adapter_3

        sqlitedb.close()

        var expandSpec =
            View.MeasureSpec.makeMeasureSpec(Int.MAX_VALUE shr 2, View.MeasureSpec.AT_MOST)
        binding.clothGrid.measure(0, expandSpec);
        var check: Int = binding.clothGrid.getMeasuredHeight()
        return check


    }


}
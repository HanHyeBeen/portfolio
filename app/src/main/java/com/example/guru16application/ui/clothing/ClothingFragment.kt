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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.guru16application.MainActivity
import com.example.guru16application.ui.ProductDBHelper
import com.example.guru16application.databinding.FragmentClothingBinding
import com.example.guru16application.member.Mypage
import com.example.guru16application.ui.clothing.Editor.ClothAddActivity


// 의 tab 메인
class ClothingFragment : Fragment() {

    private var _binding: FragmentClothingBinding? = null

    private val binding get() = _binding!!

    lateinit var Image: ByteArray

    var Relist = ArrayList<ReViewItem>()
    var Grlist = ArrayList<ReViewItem>()


    //lateinit var mainActivity: MainActivity

    lateinit var dbManager: ProductDBHelper
    lateinit var sqlitedb: SQLiteDatabase


    /*override fun onAttach(context: Context) {
        super.onAttach(context)


        mainActivity = context as MainActivity
    }*/


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val clothingViewModel = ViewModelProvider(this).get(ClothingViewModel::class.java)

        _binding = FragmentClothingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // ───────────────────────────── 글쓰기버튼 리스너 ─────────────────────────────

        val button : Button = binding.btnCreate
        button.setOnClickListener {
            val intent : Intent = Intent(context, ClothAddActivity::class.java)
            startActivity(intent)
            /*val requestLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult())
        {
            it.data!!.getStringExtra("result")?.let {
               // datas?.add(it)
               // adapter.notifyDataSetChanged()
            }
        }*/
        }

        // 보일 아이템 검색
        dbManager = ProductDBHelper(context, "food.db")

        sqlitedb = dbManager.readableDatabase
        sqlitedb = dbManager.writableDatabase

        Relist.clear()
        Grlist.clear()

        var cursor_r: Cursor
        cursor_r = sqlitedb.rawQuery("SELECT * FROM cloth WHERE cType = 1;", null)

        var cursor_g: Cursor
        cursor_g = sqlitedb.rawQuery("SELECT cMainimg, cName FROM cloth WHERE cType = 2;", null)



        while (cursor_r.moveToNext()) {
            Image = cursor_r.getBlob(cursor_r.getColumnIndexOrThrow("cMainimg"))
            val bitmap: Bitmap = BitmapFactory.decodeByteArray(Image, 0, Image.size)
            var Name = cursor_r.getString((cursor_r.getColumnIndexOrThrow("cName"))).toString()

            Relist.add(ReViewItem(bitmap, Name))


        }

        while (cursor_g.moveToNext()) {
            Image = cursor_g.getBlob(cursor_g.getColumnIndexOrThrow("cMainimg"))
            val bitmap: Bitmap = BitmapFactory.decodeByteArray(Image, 0, Image.size)
            var Name = cursor_g.getString((cursor_g.getColumnIndexOrThrow("cName"))).toString()

            Grlist.add(ReViewItem(bitmap, Name))

        }

        val recyler: RecyclerView = binding.recyclerHorizon
        val gridcloth: GridView = binding.clothGrid
        clothingViewModel.clothlist.observe(viewLifecycleOwner) {

            val relist: ArrayList<ReViewItem> = Relist
            val Adapter_2 = ReViewAdapter(requireContext(), relist)
            recyler.adapter = Adapter_2
        }

        clothingViewModel.clothlist.observe(viewLifecycleOwner) {

            val glist: ArrayList<ReViewItem> = Grlist
            val Adapter_3 = GriViewAdapter(requireContext(), glist)
            gridcloth.adapter = Adapter_3
        }


        sqlitedb.close()

        var expandSpec =
            View.MeasureSpec.makeMeasureSpec(Int.MAX_VALUE shr 2, View.MeasureSpec.AT_MOST)
        binding.clothGrid.measure(0, expandSpec)
        binding.clothGrid.getLayoutParams().height = check("SELECT * FROM cloth WHERE cType = 2;")


        //버튼 클릭 시 이벤트
        binding.clothSearchBtn.setOnClickListener {

            Relist.clear()
            Grlist.clear()
            downKeyboard()

            var cloth_text: String = binding.clothSearchEdt.text.toString()

            sqlitedb = dbManager.readableDatabase
            sqlitedb = dbManager.writableDatabase


            var cursor_rs: Cursor
            cursor_rs = sqlitedb.rawQuery("SELECT * FROM cloth WHERE cType = 1 AND (cName LIKE '%" + cloth_text + "%' OR cMore LIKE '%" + cloth_text + "%');", null)

            var cursor_gs: Cursor
            cursor_gs = sqlitedb.rawQuery("SELECT * FROM cloth WHERE cType = 2 AND (cName LIKE '%" + cloth_text + "%' OR cMore LIKE '%" + cloth_text + "%');", null)

            if(cursor_rs.count > 0){
                binding.clothNo1.visibility = View.GONE
                binding.recyclerHorizon.visibility = View.VISIBLE

                while (cursor_rs.moveToNext()) {
                    Image = cursor_rs.getBlob(cursor_rs.getColumnIndexOrThrow("cMainimg"))
                    val bitmap: Bitmap = BitmapFactory.decodeByteArray(Image, 0, Image.size)
                    var Name = cursor_rs.getString((cursor_rs.getColumnIndexOrThrow("cName"))).toString()

                    Relist.add(ReViewItem(bitmap, Name))

                }

                val recyler2: RecyclerView = binding.recyclerHorizon
                clothingViewModel.clothlist.observe(viewLifecycleOwner) {

                    val relist_c: ArrayList<ReViewItem> = Relist
                    val Adapter_2 = ReViewAdapter(requireContext(), relist_c)
                    recyler2.adapter = Adapter_2
                }
            } else {
                binding.clothNo1.visibility = View.VISIBLE
                binding.recyclerHorizon.visibility = View.GONE
            }

            if(cursor_gs.count > 0){
                binding.clothNo2.visibility = View.GONE
                binding.clothGrid.visibility = View.VISIBLE


                while (cursor_gs.moveToNext()) {

                    Image = cursor_gs.getBlob(cursor_gs.getColumnIndexOrThrow("cMainimg"))
                    val bitmap: Bitmap = BitmapFactory.decodeByteArray(Image, 0, Image.size)
                    var Name = cursor_gs.getString((cursor_gs.getColumnIndexOrThrow("cName"))).toString()

                    Grlist.add(ReViewItem(bitmap, Name))
                }

                    val gridcloth2: GridView = binding.clothGrid

                    clothingViewModel.clothlist.observe(viewLifecycleOwner) {

                        val glist_c: ArrayList<ReViewItem> = Grlist
                        val Adapter_3 = GriViewAdapter(requireContext(), glist_c)
                        gridcloth2.adapter = Adapter_3
                    }


                    var sqlsen: String = "SELECT * FROM cloth WHERE cType = 2 AND (cName LIKE '%$cloth_text%' OR cMore LIKE '%$cloth_text%');"


                    //사이즈 조절
                    var expandSpec = View.MeasureSpec.makeMeasureSpec(Int.MAX_VALUE shr 2, View.MeasureSpec.AT_MOST)
                    binding.clothGrid.measure(0, expandSpec)
                    check(sqlsen)
                    binding.clothGrid.getLayoutParams().height = check(sqlsen)

                } else {
                binding.clothNo2.visibility = View.VISIBLE
                binding.clothGrid.visibility = View.GONE }


            sqlitedb.close()

        }

        // 그리드 레이아웃 클릭 시 이벤트
        binding.clothGrid.setOnItemClickListener { adapterView, view, i, l ->
            var item:ReViewItem = Grlist[i]
            val sname = item.name

            //액티비티와 프래그먼트 연결
            var intent: Intent = Intent(context,ClothNextActivity::class.java)
            intent.putExtra("searchName",sname)
            startActivity(intent)

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

        dbManager = ProductDBHelper(context, "food.db")

        sqlitedb = dbManager.readableDatabase
        sqlitedb = dbManager.writableDatabase

        var cursor: Cursor
        cursor = sqlitedb.rawQuery(string, null)



        while (cursor.moveToNext()) {
            Image = cursor.getBlob(cursor.getColumnIndexOrThrow("cMainimg"))
            val bitmap: Bitmap = BitmapFactory.decodeByteArray(Image, 0, Image.size)
            var Name = cursor.getString((cursor.getColumnIndexOrThrow("cName"))).toString()

            Grlist.add(ReViewItem(bitmap, Name))

        }


        val gridcloth: GridView = binding.clothGrid
        val glist: ArrayList<ReViewItem> = Grlist
        val Adapter_3 = GriViewAdapter(requireContext(), glist)
        gridcloth.adapter = Adapter_3

        sqlitedb.close()

        var expandSpec =
            View.MeasureSpec.makeMeasureSpec(Int.MAX_VALUE shr 2, View.MeasureSpec.AT_MOST)
        binding.clothGrid.measure(0, expandSpec);
        var check: Int = binding.clothGrid.getMeasuredHeight()
        return check


    }

    // ───────────────────────────── 글쓰기버튼 인텐트 처리 ─────────────────────────────

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
            //if(requestCode == 0 && resultCode == Activity.R) {
            //val result = data?.getString("resultData")
        //}
    }


}
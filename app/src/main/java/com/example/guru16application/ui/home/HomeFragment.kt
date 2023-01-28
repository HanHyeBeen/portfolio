package com.example.guru16application.ui.home

import android.content.Context
import android.content.res.AssetManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.guru16application.MainActivity
import com.example.guru16application.R
import com.example.guru16application.databinding.FragmentHomeBinding
import com.example.guru16application.member.datashare
import com.example.guru16application.ui.ProductDBHelper
import com.example.guru16application.ui.home.api.Constants.Companion.TAG
import com.example.guru16application.ui.home.api.WeatherViewModel
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

// 홈 tab 메인
// 기능 : 날씨 API, todo list 작성

class HomeFragment : Fragment() {

    // ──────────────────────────────────────────────────────────── 프래그먼트 초기화

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!



    /*lateinit var todoadd : ImageButton
    lateinit var todoBtn  : Button
    lateinit var todoEdt : EditText
    lateinit var pannel : SlidingUpPanelLayout
    lateinit var recycler: RecyclerView*/

    lateinit var dbManager : ProductDBHelper
    lateinit var sqlitedb : SQLiteDatabase



    var todoarr = arrayListOf<TodoViewItem>()
    var dataid : datashare = datashare
    var logid = dataid.getValue()


    lateinit var db: SQLiteDatabase


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        // weather view
        //val weatherViewModel by viewModels<WeatherViewModel>()

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val mContext: Context = requireContext()


        /*val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
//         오류 코드
//        weatherViewModel.getWeather("JSON", 3, 1, 20230126, 0800, "62", "128")
//        weatherViewModel.weatherResponse.observe(viewLifecycleOwner){
//            for(i in it.body()?.response!!.body.items.item){
//                Log.d(TAG, "$i")
//            }
//        }



      dbManager = ProductDBHelper(context, "food.db")
        sqlitedb = dbManager.readableDatabase
        sqlitedb = dbManager.writableDatabase

        var cursor: Cursor
            cursor = sqlitedb.rawQuery("SELECT * FROM todo WHERE touser = '"+logid+"';", null)

            Toast.makeText(context,"${cursor.count}", Toast.LENGTH_SHORT).show()

            while (cursor.moveToNext()) {

                var listtext = cursor.getString((cursor.getColumnIndexOrThrow("todo"))).toString()

                todoarr.add(TodoViewItem(listtext))
            }


        val recycler : RecyclerView = binding.recy
        homeViewModel.text.observe(viewLifecycleOwner) {
            val todolist = todoarr
            val Adapter_1 = TodoViewAdapter(requireContext(), todolist)
            recycler.adapter = Adapter_1
        }




        binding.TodoBtn.setOnClickListener{

            binding.bottomPanel.panelState = SlidingUpPanelLayout.PanelState.HIDDEN
            Handler(Looper.getMainLooper()).postDelayed({
                downKeyboard()
            }, 390)



            var todo_text : String = binding.TodoEdt.text.toString()
            var todoArr = arrayListOf<TodoViewItem>()

            sqlitedb = dbManager.writableDatabase
            sqlitedb = dbManager.readableDatabase


            var cursor: Cursor
            cursor = sqlitedb.rawQuery("SELECT * FROM todo WHERE todo = '"+todo_text+"' AND touser = '"+logid+"';", null)

            if(cursor.count == 0) {

                sqlitedb.execSQL("INSERT INTO todo VALUES ('" + todo_text + "', '" + logid +"');")

                var cursor1: Cursor
                cursor1 = sqlitedb.rawQuery("SELECT * FROM todo WHERE todo = '"+todo_text+"' AND touser = '"+logid+"';", null)

                while (cursor1.moveToNext()) {

                    var listtext1 = cursor1.getString((cursor1.getColumnIndexOrThrow("todo"))).toString()

                    todoArr.add(TodoViewItem(listtext1))

                }

                val recycler_s: RecyclerView = binding.recy
                homeViewModel.text.observe(viewLifecycleOwner) {
                    val modelist: ArrayList<TodoViewItem> = todoArr
                    val Adapter_s = TodoViewAdapter(requireContext(), modelist)
                    Adapter_s.notifyDataSetChanged()
                    recycler_s.adapter = Adapter_s
                }

                sqlitedb.close()

                binding.TodoEdt.text = null


            }else{

                binding.TodoEdt.text = null
                Toast.makeText(context, "이미 존재하는 일정입니다.", Toast.LENGTH_SHORT).show()

            }



        }

        binding.todoadd.setOnClickListener{

            binding.bottomPanel.panelState = SlidingUpPanelLayout.PanelState.EXPANDED

        }

        binding.todore.setOnClickListener{
            sqlitedb = dbManager.readableDatabase
            sqlitedb = dbManager.writableDatabase

            var todore = arrayListOf<TodoViewItem>()

            var cursor_r: Cursor
            cursor_r = sqlitedb.rawQuery("SELECT * FROM todo WHERE touser = '"+logid+"';", null)

            Toast.makeText(context,"갱신되었습니다.", Toast.LENGTH_SHORT).show()

            while (cursor_r.moveToNext()) {

                var listtext = cursor_r.getString((cursor_r.getColumnIndexOrThrow("todo"))).toString()

                todore.add(TodoViewItem(listtext))
            }


            val recycler_r : RecyclerView = binding.recy
            homeViewModel.text.observe(viewLifecycleOwner) {
                val todolist = todore
                val Adapter_r = TodoViewAdapter(requireContext(), todolist)
                Adapter_r.notifyDataSetChanged()
                recycler_r.adapter = Adapter_r
            }

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

}
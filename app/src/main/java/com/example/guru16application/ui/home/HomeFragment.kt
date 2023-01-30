package com.example.guru16application.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.AssetManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Build
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.guru16application.MainActivity
import com.example.guru16application.R
import com.example.guru16application.databinding.FragmentHomeBinding
import com.example.guru16application.member.datashare
import com.example.guru16application.ui.ProductDBHelper
import com.example.guru16application.ui.home.api.*
import com.example.guru16application.ui.home.api.Constants.Companion.TAG
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList

// 홈 tab 메인
// 기능 : 날씨 API, todo list 작성

class HomeFragment : Fragment() {

    // ──────────────────────────────────────────────────────────── 변수 선언

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var baseDate = "yyyyMMdd"
    private var baseTime = "0030"

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

    // ──────────────────────────────────────────────────────────── 프래그먼트 초기화

    @SuppressLint("SetText|18n", "MissingPermission")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        // ──────────────────────────── 날씨 설정 부분 ────────────────────────────

        binding.tvDate.text = SimpleDateFormat("MM월 dd일", Locale.getDefault()).format(Calendar.getInstance().time)
        requestData()

        // ──────────────────────────────────────────────────────────────────────
        val mContext: Context = requireContext()

        dbManager = ProductDBHelper(context, "food.db")
        sqlitedb = dbManager.readableDatabase
        sqlitedb = dbManager.writableDatabase

        todoarr.clear()

        var cursor: Cursor
            cursor = sqlitedb.rawQuery("SELECT * FROM todo WHERE touser = '"+logid+"';", null)

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
                cursor1 = sqlitedb.rawQuery("SELECT * FROM todo WHERE touser = '"+logid+"';", null)

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

    // ──────────────────────────────────────────────────────────── 첫 번째 레이아웃 : 날씨 정보

    private fun setWeather() {

        // 시간 정보 가져오기
        val cal = java.util.Calendar.getInstance()
        baseDate = java.text.SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time)
        val timeH = java.text.SimpleDateFormat("HH", Locale.getDefault()).format(cal.time)
        val timeM = java.text.SimpleDateFormat("HH", Locale.getDefault()).format(cal.time)

        baseTime = WeatherSet().getBaseTime(timeH, timeM)
        if ((timeH == "00") && (baseTime == "2330")) {
            cal.add(java.util.Calendar.DATE, -1).toString()
            baseDate = java.text.SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time)
        }

        // 날씨 가져오기
        val call =
            AppModule.provideWeatherAPI().getWeather("JSON", 60, 1, baseDate, baseTime, 62, 128)

        call.enqueue(object : retrofit2.Callback<Weather> {
            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                if (response.isSuccessful) {
                    val it: List<Weather.Item> = response!!.body()!!.response.body.items.item
                    val weatherArray = arrayOf(ModelWeather())

                    var index = 0
                    when (it[0].category) {
                        "PTY" -> weatherArray[index].rainType = it[0].fcstValue
                        "SKY" -> weatherArray[index].sky = it[0].fcstValue
                        "T1H" -> weatherArray[index].temp = it[0].fcstValue
                    }
                    binding.weatherRecyclerView.adapter = WeatherAdapter(weatherArray)
                }
            }

            override fun onFailure(call: Call<Weather>, t: Throwable) {
                binding.tvError.text = "api failure : " + t.message.toString() + "\n 인터넷 연결이 불안정 합니다"
                binding.tvError.visibility = View.VISIBLE
            }
        })
    }

    @SuppressLint("MissingPermission")
    private fun requestData() {

        binding.tvDate.text = SimpleDateFormat("MM월 dd일", Locale.getDefault()).format(
            Calendar.getInstance().time)
        setWeather()

    }
    // ──────────────────────────────────────────────────────────── 두 번째 레이아웃 : 할일 목록
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

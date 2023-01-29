package com.example.guru16application.ui.food

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.guru16application.MainActivity
import com.example.guru16application.R
import com.example.guru16application.databinding.FragmentFoodBinding
import com.example.guru16application.databinding.FragmentFoodBinding.*
import com.example.guru16application.ui.ProductDBHelper
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import net.daum.mf.map.api.CalloutBalloonAdapter
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

// 식 tab 메인

class FoodFragment : Fragment(), MapView.POIItemEventListener {

    private var _binding: FragmentFoodBinding? = null
    var lastlist = arrayListOf<ListViewItem>()

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

        var map: MapView = MapView(activity)
        var mapcontainer: ViewGroup = binding.foodmap as ViewGroup

        map.setMapCenterPointAndZoomLevel(
            MapPoint.mapPointWithGeoCoord(37.62709199754345, 127.09118059101046),
            2,
            true
        )
        map.setCalloutBalloonAdapter(CustumBallonAdapter(layoutInflater,mainActivity ))
        map.setPOIItemEventListener(this)
        val markerArr = ArrayList<MapPOIItem>()

        //db 관련 코드 : 데이터가 있는 db 접속하기
        dbManager = ProductDBHelper(mainActivity, "food.db")
        sqlitedb = dbManager.readableDatabase
        sqlitedb = dbManager.writableDatabase

        var cursor: Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM food ;", null)

        while (cursor.moveToNext()) {
            Image = cursor.getBlob(cursor.getColumnIndexOrThrow("fimg"))
            val bitmap: Bitmap = BitmapFactory.decodeByteArray(Image, 0, Image.size)
            var Name = cursor.getString((cursor.getColumnIndexOrThrow("fName"))).toString()
            var Loc = cursor.getString((cursor.getColumnIndexOrThrow("fLoc"))).toString()

            //영업시간
            var startT = cursor.getInt((cursor.getColumnIndexOrThrow("fstartTime")))
            var startM = cursor.getInt((cursor.getColumnIndexOrThrow("fstartmin")))
            var finT = cursor.getInt((cursor.getColumnIndexOrThrow("ffinTime")))
            var finM = cursor.getInt((cursor.getColumnIndexOrThrow("ffinmin")))

            var Time = "시작 시간 : ${startT} 시 ${startM} 분\n종료 시간 : ${finT} 시 ${finM} 분"
            list.add(ListViewItem(bitmap, Name, Loc, Time))

            var flat = cursor.getDouble(cursor.getColumnIndexOrThrow("flat"))
            var flong = cursor.getDouble(cursor.getColumnIndexOrThrow("flong"))

            var marker: MapPOIItem = MapPOIItem()
            marker.itemName = Name
            marker.mapPoint = MapPoint.mapPointWithGeoCoord(flat, flong)
            markerArr.add(marker)
        }

        val arr = arrayOfNulls<MapPOIItem>(markerArr.size)
        map.addPOIItems(markerArr.toArray(arr))

        mapcontainer.addView(map)

        val listView1: ListView = binding.listView
        notificationsViewModel.list_n.observe(viewLifecycleOwner) {

            val modelist: ArrayList<ListViewItem> = list
            val Adapter_1 = ListViewAdapter(mainActivity, modelist)
            listView1.adapter = Adapter_1
        }
        lastlist = list
        sqlitedb.close()

        binding.bottomFoodPanel.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED


        binding.foodSearchBtn.setOnClickListener {

            binding.bottomFoodPanel.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
            Handler(Looper.getMainLooper()).postDelayed({
                //실행 코드
                downKeyboard()
            }, 300)


            list.clear()
            mapcontainer.removeAllViews()

            var sermap: MapView = MapView(activity)
            sermap.setMapCenterPointAndZoomLevel(
                MapPoint.mapPointWithGeoCoord(37.62709199754345, 127.09118059101046),
                2,
                true
            )
            sermap.setCalloutBalloonAdapter(CustumBallonAdapter(layoutInflater, mainActivity))
            sermap.setPOIItemEventListener(this)

            //Toast.makeText(mainActivity,"됨", Toast.LENGTH_SHORT).show()

            var str_text: String = binding.foodSearchEdt.text.toString()
            var searchArr = ArrayList<MapPOIItem>()

            sqlitedb = dbManager.readableDatabase
            sqlitedb = dbManager.writableDatabase

            if (str_text != "") {
                cursor =
                    sqlitedb.rawQuery(
                        "SELECT * FROM food WHERE fLoc LIKE '%" + str_text + "%' OR fName LIKE '%" + str_text + "%';",
                        null
                    )
            } else {
                cursor = sqlitedb.rawQuery("SELECT * FROM food;", null)
            }

            val rowCount: Int = cursor.count


            if (rowCount != 0) {

                binding.listView.visibility = View.VISIBLE
                binding.noSearch.visibility = View.GONE


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
                    list.add(ListViewItem(bitmap, Name, Loc, Time))


                    var flat = cursor.getDouble(cursor.getColumnIndexOrThrow("flat"))
                    var flong = cursor.getDouble(cursor.getColumnIndexOrThrow("flong"))

                    var marker_s: MapPOIItem = MapPOIItem()
                    marker_s.itemName = Name
                    marker_s.mapPoint = MapPoint.mapPointWithGeoCoord(flat, flong)
                    searchArr.add(marker_s)
                }

                val listView2: ListView = binding.listView
                notificationsViewModel.list_n.observe(viewLifecycleOwner) {

                    val modelist: ArrayList<ListViewItem> = list
                    val Adapter_1 = ListViewAdapter(mainActivity, modelist)
                    listView2.adapter = Adapter_1
                }
                lastlist = list

                sqlitedb.close()

                val sarr = arrayOfNulls<MapPOIItem>(searchArr.size)
                sermap.addPOIItems(searchArr.toArray(sarr))

                mapcontainer.addView(sermap)

                if (rowCount == 1) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        //실행 코드
                        binding.bottomFoodPanel.panelState =
                            SlidingUpPanelLayout.PanelState.EXPANDED
                    }, 400)
                }

            } else {
                binding.listView.visibility = View.GONE
                binding.noSearch.visibility = View.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed({
                    //실행 코드
                    binding.bottomFoodPanel.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
                }, 400)
                sermap.addPOIItems(markerArr.toArray(arr))
                mapcontainer.addView(sermap)

            }

            //기존 데이터를 없애는 코드
            val new: EditText = binding.foodSearchEdt
            notificationsViewModel.text.observe(viewLifecycleOwner) {
                new.text = null
            }


        }

        binding.listView.setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, view, i, l ->
            var item: ListViewItem = lastlist[i]
            val fname = item.name

            //액티비티와 프래그먼트 연결
            var intent: Intent = Intent(context, FoodNextActivity::class.java)
            intent.putExtra("searchfName", fname)
            startActivity(intent)

        })



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class CustumBallonAdapter(inflater: LayoutInflater, context: Context) : CalloutBalloonAdapter {
        val food_box: View = inflater.inflate(R.layout.food_box, null)
        var foodboxname: TextView = food_box.findViewById(R.id.foodboxname)
        var foodthem: TextView = food_box.findViewById(R.id.foodthem)
        var text: String = ""

        val dbManager = ProductDBHelper(context, "food.db")
        var sqlitedb = dbManager.readableDatabase


        override fun getCalloutBalloon(poiItem: MapPOIItem?): View {
            foodboxname.text = poiItem?.itemName

            var fname_poi = poiItem?.itemName
            var fcursor: Cursor
            fcursor = sqlitedb.rawQuery("SELECT * FROM food WHERE fName = '" + fname_poi  + "';", null)


            while (fcursor.moveToNext()) {

                var type = fcursor.getString((fcursor.getColumnIndexOrThrow("ftype"))).toString()

                foodthem.text = type

            }

            return food_box
        }

        override fun getPressedCalloutBalloon(poiItem: MapPOIItem?): View {
            foodboxname.text = poiItem?.itemName
            foodthem.text = text

            return food_box

        }
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

    override fun onPOIItemSelected(p0: MapView?, poiItem: MapPOIItem?) {
        binding.bottomFoodPanel.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED

        val dbManager = ProductDBHelper(mainActivity, "food.db")
        var sqlitedb = dbManager.readableDatabase
        sqlitedb = dbManager.writableDatabase

        var Image: ByteArray
        var list = arrayListOf<ListViewItem>()

        var item = poiItem?.itemName

        var fcursor: Cursor
        fcursor = sqlitedb.rawQuery("SELECT * FROM food WHERE fName = '" + item + "';", null)

        binding.listView.visibility = View.VISIBLE
        binding.noSearch.visibility = View.GONE

        while (fcursor.moveToNext()) {
            Image = fcursor.getBlob(fcursor.getColumnIndexOrThrow("fimg"))
            val bitmap: Bitmap = BitmapFactory.decodeByteArray(Image, 0, Image.size)
            var Name = fcursor.getString((fcursor.getColumnIndexOrThrow("fName"))).toString()
            var Loc = fcursor.getString((fcursor.getColumnIndexOrThrow("fLoc"))).toString()
            var startT = fcursor.getInt((fcursor.getColumnIndexOrThrow("fstartTime")))
            var startM = fcursor.getInt((fcursor.getColumnIndexOrThrow("fstartmin")))
            var finT = fcursor.getInt((fcursor.getColumnIndexOrThrow("ffinTime")))
            var finM = fcursor.getInt((fcursor.getColumnIndexOrThrow("ffinmin")))

            var Time = "시작 시간 : ${startT} 시 ${startM} 분\n종료 시간 : ${finT} 시 ${finM} 분"
            list.add(ListViewItem(bitmap, Name, Loc, Time))


        }

        val listView_f: ListView = binding.listView
        val modelist: ArrayList<ListViewItem> = list
        lastlist = list
        val Adapter_1 = ListViewAdapter(mainActivity, modelist)
        listView_f.adapter = Adapter_1


        sqlitedb.close()


        Handler(Looper.getMainLooper()).postDelayed({
            //실행 코드
            binding.bottomFoodPanel.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
        }, 400)


    }

    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, poiItem: MapPOIItem?) {
    }

    override fun onCalloutBalloonOfPOIItemTouched(
        p0: MapView?,
        poiItem: MapPOIItem?,
        p2: MapPOIItem.CalloutBalloonButtonType?
    ) {

    }

    override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {

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
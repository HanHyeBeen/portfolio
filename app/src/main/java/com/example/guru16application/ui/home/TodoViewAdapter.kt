package com.example.guru16application.ui.home

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.guru16application.MainActivity
import com.example.guru16application.R
import com.example.guru16application.ui.ProductDBHelper
import com.example.guru16application.ui.clothing.ClothNextActivity
import kotlinx.coroutines.NonDisposableHandle.parent


class TodoViewAdapter (val context: Context, val Relist: ArrayList<TodoViewItem>): RecyclerView.Adapter<TodoViewAdapter.ViewHolder>() {

    lateinit var sqlitdb : SQLiteDatabase


    inner class ViewHolder(view: View?) : RecyclerView.ViewHolder(view!!){
        var todore = view?.findViewById<ImageButton>(R.id.todoremove)
        var todo = view?.findViewById<TextView>(R.id.todo)

        fun bind(room:TodoViewItem, context: Context, onClickListener: View.OnClickListener){

            todo?.text = room.name
            todore?.setOnClickListener(onClickListener)


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.todo_list,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val citem = Relist[position]

        holder.apply {
            bind(citem, context, View.OnClickListener {

                var dbManager = ProductDBHelper(context, "food.db")
                sqlitdb = dbManager.readableDatabase
                sqlitdb.execSQL("DELETE FROM todo WHERE todo = '" + citem.name + "';")
                sqlitdb.close()
                dbManager.close()

                Toast.makeText(context,"${position+1} 번 항목 삭제! 갱신 버튼을 클릭하세요.", Toast.LENGTH_SHORT).show()








            })
        }


    }

    interface OnItemClickListener{
        fun onClick(v:View, position: Int)
    }


    fun setItemClickListener(itemClickListener: OnItemClickListener){
        this.itemClickListener = itemClickListener
    }

    private lateinit var itemClickListener : OnItemClickListener




    override fun getItemCount(): Int = Relist.size

}
package com.example.guru16application.ui.food

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ProductDBHelper(context: Context?, name: String?) : SQLiteOpenHelper(context, "food.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        // TODO Auto-generated method stub
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // TODO Auto-generated method stub
    }

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.disableWriteAheadLogging()
    }
}
package com.example.guru16application.member

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

//최초 생성할 memberDB
class DBManager(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE memberTBL (name text, phone text, id text PRIMARY KEY, pw text)")
        db!!.execSQL("INSERT INTO memberTBL VALUES ('Admin', '01012345678', 'swuswu', '1234');")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }


}
package com.example.githubuserbfaa.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.githubuserbfaa.db.UserContract.UserColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context):SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    companion object{
        private const val DATABASE_NAME = "dbfavorite"
        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_USER = "CREATE TABLE $TABLE_NAME" +
                "(${UserContract.UserColumns.USERNAME} TEXT PRIMARY KEY,"+
                "${UserContract.UserColumns.AVATAR} TEXT NOT NULL,"+
                "${UserContract.UserColumns.COMPANY} TEXT NOT NULL,"+
                "${UserContract.UserColumns.LOCATION} TEXT NOT NULL,"+
                "${UserContract.UserColumns.NAME} TEXT NOT NULL,"+
                "${UserContract.UserColumns.TOTALFLR} TEXT NOT NULL,"+
                "${UserContract.UserColumns.BLOG} TEXT NOT NULL,"+
                "${UserContract.UserColumns.TOTALFLW} TEXT NOT NULL,"+
                "${UserContract.UserColumns.FAVORITE} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_USER)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}
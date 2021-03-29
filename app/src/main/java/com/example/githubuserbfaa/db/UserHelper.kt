package com.example.githubuserbfaa.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.githubuserbfaa.db.UserContract.UserColumns.Companion.TABLE_NAME
import com.example.githubuserbfaa.db.UserContract.UserColumns.Companion.USERNAME
import java.sql.SQLException

class UserHelper(context: Context) {

    private var databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private var database: SQLiteDatabase = databaseHelper.writableDatabase

    companion object{
        private const val DATABASE_TABLE = TABLE_NAME

        private var INSTANCE: UserHelper? = null
        fun getInstance(context: Context): UserHelper =
                INSTANCE ?: synchronized(this){
                    INSTANCE ?: UserHelper(context)
                }
    }

    @Throws(SQLException::class)
    fun open(){
        database = databaseHelper.writableDatabase
    }

    fun close(){
        databaseHelper.close()

        if (database.isOpen)
            database.close()
    }
    fun queryAll():Cursor{
        return database.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                "$USERNAME ASC"

        )
    }
    fun queryById(id: String): Cursor{
        return database.query(
                DATABASE_TABLE,
                null,
                "$USERNAME = ?",
                arrayOf(id),
                null,
                null,
                null,
                null,
        )
    }
    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE,null,values)
    }
    fun update(id: String, values: ContentValues?):Int{
        return database.update(DATABASE_TABLE, values, "$USERNAME = ?", arrayOf(id))
    }
    fun deleteById(id: String): Int{
        return database.delete(DATABASE_TABLE, "$USERNAME = '$id'", null)
    }
}
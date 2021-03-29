package com.example.githubuserbfaa.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.githubuserbfaa.db.UserContract.AUTHORITY
import com.example.githubuserbfaa.db.UserContract.UserColumns.Companion.CONTENT_URI
import com.example.githubuserbfaa.db.UserContract.UserColumns.Companion.TABLE_NAME
import com.example.githubuserbfaa.db.UserHelper

class ProviderFav: ContentProvider() {

    companion object {
        private const val FAVORITE = 1
        private const val FAVORITE_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var favoriteHelper: UserHelper

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAVORITE)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", FAVORITE_ID)
        }
    }

    override fun onCreate(): Boolean {
        favoriteHelper = UserHelper.getInstance(context as Context)
        favoriteHelper.open()
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        return when (sUriMatcher.match(uri)){
            FAVORITE -> favoriteHelper.queryAll()
            FAVORITE_ID -> favoriteHelper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun getType(uri: Uri): String? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (FAVORITE){
            sUriMatcher.match(uri) -> favoriteHelper.insert(values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val delete: Int = when(FAVORITE_ID){
            sUriMatcher.match(uri) -> favoriteHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return delete
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        val update: Int = when(FAVORITE_ID){
            sUriMatcher.match(uri) -> favoriteHelper.update(uri.lastPathSegment.toString(),values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return update
    }

}
package com.example.githubuserbfaa.helper

import android.database.Cursor
import com.example.githubuserbfaa.User
import com.example.githubuserbfaa.db.UserContract.UserColumns.Companion.AVATAR
import com.example.githubuserbfaa.db.UserContract.UserColumns.Companion.BLOG
import com.example.githubuserbfaa.db.UserContract.UserColumns.Companion.COMPANY
import com.example.githubuserbfaa.db.UserContract.UserColumns.Companion.FAVORITE
import com.example.githubuserbfaa.db.UserContract.UserColumns.Companion.LOCATION
import com.example.githubuserbfaa.db.UserContract.UserColumns.Companion.NAME
import com.example.githubuserbfaa.db.UserContract.UserColumns.Companion.TOTALFLR
import com.example.githubuserbfaa.db.UserContract.UserColumns.Companion.TOTALFLW
import com.example.githubuserbfaa.db.UserContract.UserColumns.Companion.USERNAME

object MappingHelper {
    fun mapCursorArrayList(notesCursor: Cursor?): ArrayList<User>{
        val favoriteList = ArrayList<User>()

        notesCursor?.apply {
            while (moveToNext()){
                val username = getString(getColumnIndexOrThrow(USERNAME))
                val name = getString(getColumnIndexOrThrow(NAME))
                val avatar = getString(getColumnIndexOrThrow(AVATAR))
                val company = getString(getColumnIndexOrThrow(COMPANY))
                val location = getString(getColumnIndexOrThrow(LOCATION))
                val favorite = getString(getColumnIndexOrThrow(FAVORITE))
                val totalflr = getString(getColumnIndexOrThrow(TOTALFLR))
                val totalflw = getString(getColumnIndexOrThrow(TOTALFLW))
                val blog = getString(getColumnIndexOrThrow(BLOG))
                favoriteList.add(
                        User(username, avatar, name, location, company, totalflr, totalflw, blog, favorite)
                )
            }
        }
        return favoriteList
    }
}


package com.example.githubuserbfaa.db

import android.net.Uri
import android.provider.BaseColumns


object UserContract {

    const val AUTHORITY = "com.example.githubuserbfaa"
    const val SCHEME = "content"

    class UserColumns : BaseColumns {
        companion object{
            const val TABLE_NAME = "favorite"
            const val NAME = "name"
            const val AVATAR = "avatar"
            const val FAVORITE = "isFavorite"
            const val COMPANY = "company"
            const val LOCATION = "location"
            const val USERNAME = "username"
            const val TOTALFLR = "totalflr"
            const val TOTALFLW = "totalflw"
            const val BLOG = "blog"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                    .authority(AUTHORITY)
                    .appendPath(TABLE_NAME)
                    .build()
        }
    }
}
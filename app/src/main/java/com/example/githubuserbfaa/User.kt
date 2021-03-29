package com.example.githubuserbfaa

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class User(
    var username: String? = null,
    var avatar: String? = null,
    var name: String? = null,
    var location: String? = null,
    var company: String? = null,
    var totalFollower: String? = null,
    var totalFollowing: String? = null,
    var blog: String? = null,
    var follower: String? = null,
    var following: String? = null,
    var favorite: String? = null
): Parcelable
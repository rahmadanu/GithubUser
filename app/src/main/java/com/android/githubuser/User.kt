package com.android.githubuser

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
       val name: String,
       val userName: String,
       val avatar: Int,
       val location: String,
       val followers: String,
       val following: String,
       val repository: String,
       val company: String
) : Parcelable

package com.android.githubuser.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "user")
@Parcelize
class UserEntity(
    @field:ColumnInfo(name = "username")
    @field:PrimaryKey
    val username: String,

    @field:ColumnInfo(name = "name")
    val name: String? = "",

    @field:ColumnInfo(name = "avatarUrl")
    val avatarUrl: String,

    @field:ColumnInfo(name = "followers")
    val followers: Int,

    @field:ColumnInfo(name = "following")
    val following: Int,

    @field:ColumnInfo(name = "public_repos")
    val repository: Int,

    @field:ColumnInfo(name = "location")
    var location: String?,

    @field:ColumnInfo(name = "company")
    var company: String?,

    @field:ColumnInfo(name = "html_url")
    val url: String,

    @field:ColumnInfo(name = "favorite")
    var isFavorite: Boolean
) : Parcelable
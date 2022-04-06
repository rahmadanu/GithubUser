package com.android.githubuser.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserResponse(

	@field:SerializedName("items")
	val items: ArrayList<Items>
)

data class Items(

	@field:SerializedName("login")
	val username: String,

	@field:SerializedName("name")
	var name: String,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

	@field:SerializedName("followers")
	val followers: Int,

	@field:SerializedName("following")
	val following: Int,

	@field:SerializedName("public_repos")
	val repository: Int,

	@field:SerializedName("location")
	var location: String,

	@field:SerializedName("company")
	var company: String,

	@field:SerializedName("following_url")
	val followingUrl: String,

	@field:SerializedName("followers_url")
	val followersUrl: String,

	@field:SerializedName("html_url")
	val url: String
)
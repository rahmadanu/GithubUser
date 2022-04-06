package com.android.githubuser.data.remote.retrofit

import com.android.githubuser.BuildConfig
import com.android.githubuser.data.remote.response.Items
import com.android.githubuser.data.remote.response.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {

    @GET("search/users")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    suspend fun getUserSearch(
        @Query("q") username: String
    ): UserResponse

    @GET("users/{username}")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    suspend fun getUserDetail(
        @Path("username") username: String
    ): Items

    @GET("users/{username}/followers")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<ArrayList<Items>>

    @GET("users/{username}/following")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<ArrayList<Items>>
}
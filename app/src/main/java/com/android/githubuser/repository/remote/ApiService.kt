package com.android.githubuser.repository.remote

import com.android.githubuser.model.Items
import com.android.githubuser.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getUserSearch(
        @Query("q") username: String
    ): Call<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<Items>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<ArrayList<Items>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<ArrayList<Items>>
}
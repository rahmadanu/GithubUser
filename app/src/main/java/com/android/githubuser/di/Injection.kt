package com.android.githubuser.di

import android.content.Context
import com.android.githubuser.data.local.room.UserDatabase
import com.android.githubuser.data.remote.retrofit.ApiConfig

object Injection {

    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val database = UserDatabase.getInstance(context)
        val dao = database.userDao()
        return UserRepository.getInstance(apiService, dao)
    }
}
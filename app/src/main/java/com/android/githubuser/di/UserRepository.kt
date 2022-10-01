package com.android.githubuser.di

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.android.githubuser.data.local.entity.UserEntity
import com.android.githubuser.data.local.room.UserDao
import com.android.githubuser.data.remote.retrofit.ApiService

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userDao: UserDao
){
    fun getUser(query: String): LiveData<Result<List<UserEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getUserSearch(query)
            val items = response.items
            val itemsDetail = items.map { user ->
                apiService.getUserDetail(user.username) }
            val userList = itemsDetail.map { user ->
                val isFavorite = userDao.isFavoriteUser(user.username)
                val empty = ""
                UserEntity(
                    user.username,
                    user.name ?: empty,
                    user.avatarUrl,
                    user.followers,
                    user.following,
                    user.repository,
                    user.location ?: empty,
                    user.company ?: empty,
                    user.url,
                    isFavorite
                )
            }
            emitSource(MutableLiveData(Result.Success(userList)))

        } catch (e: Exception) {
            Log.d(TAG, "getUser: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
   }

    fun isFavorite(username: String): LiveData<Boolean> = liveData {
        emit(userDao.isFavoriteUser(username))
    }

    suspend fun insertUser(user: UserEntity, favoriteState: Boolean) {
        user.isFavorite = favoriteState
        userDao.insertUser(user)
    }

    suspend fun deleteUser(user: UserEntity, favoriteState: Boolean) {
        user.isFavorite = favoriteState
        userDao.deleteUser(user)
    }

    fun getFavoriteUser(): LiveData<List<UserEntity>> {
        return userDao.getFavoriteUser()
    }

    companion object {
        private const val TAG = "UserRepository"

        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            apiService: ApiService,
            userDao: UserDao
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userDao)
            }.also { instance = it }
    }
}
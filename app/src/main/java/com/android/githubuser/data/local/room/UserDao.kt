package com.android.githubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.githubuser.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getUser(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM user where favorite = 1")
    fun getFavoriteUser(): LiveData<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("DELETE FROM user")
    suspend fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM user WHERE username = :username AND favorite = 1)")
    suspend fun isFavoriteUser(username: String): Boolean
}
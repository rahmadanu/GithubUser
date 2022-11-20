package com.android.githubuser.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.githubuser.data.local.entity.UserEntity
import com.android.githubuser.di.UserRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun isFavorite(username: String) = userRepository.isFavorite(username)

    fun insertUser(user: UserEntity) {
        viewModelScope.launch {
            userRepository.insertUser(user, true)
        }
    }

    fun deleteUser(user: UserEntity) {
        viewModelScope.launch {
            userRepository.deleteUser(user, false)
        }
    }
}
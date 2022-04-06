package com.android.githubuser.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.githubuser.di.UserRepository
import com.android.githubuser.data.local.entity.UserEntity
import kotlinx.coroutines.launch

class FavoriteViewModel(private val userRepository: UserRepository): ViewModel() {

    fun getFavoriteUser() = userRepository.getFavoriteUser()

    fun deleteUser(user: UserEntity) {
        viewModelScope.launch {
            userRepository.deleteUser(user, false)
        }
    }
}
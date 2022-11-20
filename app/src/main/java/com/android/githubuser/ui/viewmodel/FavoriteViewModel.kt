package com.android.githubuser.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.android.githubuser.di.UserRepository

class FavoriteViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getFavoriteUser() = userRepository.getFavoriteUser()
}
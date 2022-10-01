package com.android.githubuser.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.githubuser.di.UserRepository
import com.android.githubuser.data.local.entity.UserEntity
import kotlinx.coroutines.launch


class MainViewModel(private val userRepository: UserRepository): ViewModel() {

    fun getUser(query: String) = userRepository.getUser(query)

    companion object {
        private const val TAG ="MainViewModel"
    }
}
package com.android.githubuser.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.githubuser.data.local.entity.UserEntity
import com.android.githubuser.di.UserRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val userRepository: UserRepository): ViewModel() {

    private val _isFavorites = MutableLiveData<Boolean>()
    val isFavorites: LiveData<Boolean>
        get() = _isFavorites

    fun isFavorite(username: String) {
        _isFavorites.value = userRepository.isFavorite(username).value
    }

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

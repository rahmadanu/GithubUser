package com.android.githubuser.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.android.githubuser.di.UserRepository


class MainViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getUser(query: String) = userRepository.getUser(query)

}
package com.android.githubuser.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.githubuser.data.remote.response.Items
import com.android.githubuser.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersFollowingViewModel: ViewModel() {

    private val userFollowers = MutableLiveData<ArrayList<Items>>()
    private val userFollowing = MutableLiveData<ArrayList<Items>>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _hasNoFollowers = MutableLiveData<Boolean>()
    val hasNoFollowers: LiveData<Boolean> = _hasNoFollowers

    private val _hasNoFollowing = MutableLiveData<Boolean>()
    val hasNoFollowing: LiveData<Boolean> = _hasNoFollowing

    fun findUserFollowing(username: String) {
        _isLoading.value = true
        _hasNoFollowing.value = false
        val client = ApiConfig.getApiService().getUserFollowing(username)
        client.enqueue(object: Callback<ArrayList<Items>> {
            override fun onResponse(call: Call<ArrayList<Items>>, response: Response<ArrayList<Items>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    userFollowing.postValue(response.body())
                    if (response.body()?.isEmpty() == true) {
                        _hasNoFollowing.value = true
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<Items>>, t: Throwable) {
                _isLoading.value = false
                _hasNoFollowing.value = true
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun findUserFollowers(username: String) {
         _isLoading.value = true
         _hasNoFollowers.value = false
         val client = ApiConfig.getApiService().getUserFollowers(username)
         client.enqueue(object: Callback<ArrayList<Items>> {
             override fun onResponse(call: Call<ArrayList<Items>>, response: Response<ArrayList<Items>>) {
                 _isLoading.value = false
                 if (response.isSuccessful) {
                     userFollowers.postValue(response.body())
                     if (response.body()?.isEmpty() == true) {
                         _hasNoFollowers.value = true
                     }
                 } else {
                     Log.e(TAG, "onFailure: ${response.message()}")
                 }
             }

             override fun onFailure(call: Call<ArrayList<Items>>, t: Throwable) {
                 _isLoading.value = false
                 _hasNoFollowers.value = true
                 Log.e(TAG, "onFailure: ${t.message}")
             }
         })
     }

    fun getFollowersUser(): LiveData<ArrayList<Items>> {
        return userFollowers
    }

    fun getFollowingUser(): LiveData<ArrayList<Items>> {
        return userFollowing
    }

    companion object {
        private const val TAG = "FollowViewModel"
    }
}
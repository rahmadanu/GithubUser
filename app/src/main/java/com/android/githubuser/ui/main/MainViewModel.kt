package com.android.githubuser.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.githubuser.network.model.Items
import com.android.githubuser.network.model.UserResponse
import com.android.githubuser.network.remote.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel : ViewModel() {

    private val listUser = MutableLiveData<ArrayList<Items>>()
    private val userDetail = MutableLiveData<Items>()
    private val userFollowers = MutableLiveData<ArrayList<Items>>()
    private val userFollowing = MutableLiveData<ArrayList<Items>>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLoadingDetail = MutableLiveData<Boolean>()
    val isLoadingDetail: LiveData<Boolean> = _isLoadingDetail

    private val _hasNoData = MutableLiveData<Boolean>()
    val hasNoData: LiveData<Boolean> = _hasNoData

    private val _hasNoFollowers = MutableLiveData<Boolean>()
    val hasNoFollowers: LiveData<Boolean> = _hasNoFollowers

    private val _hasNoFollowing = MutableLiveData<Boolean>()
    val hasNoFollowing: LiveData<Boolean> = _hasNoFollowing

    private val _hasConnectionFailed = MutableLiveData<Boolean>()
    val hasConnectionFailed: LiveData<Boolean> = _hasConnectionFailed

    fun findUsername(query: String) {
        _isLoading.value = true
        _hasNoData.value = false
        val client = ApiConfig.getApiService().getUserSearch(query)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    listUser.postValue(response.body()?.items)
                    if (response.body()?.items?.isEmpty() == true) {
                        _hasNoData.value = true
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = true
                _hasNoData.value = false
                _hasConnectionFailed.value = true
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun findUserDetail(username: String) {
        _isLoadingDetail.value = true
        val client = ApiConfig.getApiService().getUserDetail(username)
        client.enqueue(object : Callback<Items> {
            override fun onResponse(call: Call<Items>, response: Response<Items>) {
                _isLoadingDetail.value = false
                _hasConnectionFailed.value = false
                if (response.isSuccessful) {
                    userDetail.postValue(response.body())
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Items>, t: Throwable) {
                _isLoadingDetail.value = false
                _hasConnectionFailed.value = true
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun findUserFollowers(username: String) {
        _isLoading.value = true
        _hasNoFollowers.value = false
        val client = ApiConfig.getApiService().getUserFollowers(username)
        client.enqueue(object : Callback<ArrayList<Items>> {
            override fun onResponse(
                call: Call<ArrayList<Items>>,
                response: Response<ArrayList<Items>>
            ) {
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

    fun findUserFollowing(username: String) {
        _isLoading.value = true
        _hasNoFollowing.value = false
        val client = ApiConfig.getApiService().getUserFollowing(username)
        client.enqueue(object : Callback<ArrayList<Items>> {
            override fun onResponse(
                call: Call<ArrayList<Items>>,
                response: Response<ArrayList<Items>>
            ) {
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

    fun getListUser(): LiveData<ArrayList<Items>> {
        return listUser
    }

    fun getDetailUser(): LiveData<Items> {
        return userDetail
    }

    fun getFollowersUser(): LiveData<ArrayList<Items>> {
        return userFollowers
    }

    fun getFollowingUser(): LiveData<ArrayList<Items>> {
        return userFollowing
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}
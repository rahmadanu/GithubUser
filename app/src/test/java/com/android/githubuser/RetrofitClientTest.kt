package com.android.githubuser

import com.android.githubuser.data.remote.retrofit.ApiConfig
import org.junit.Assert
import org.junit.Test

class RetrofitClientTest {
    @Test
    fun testRetrofitInstance() {
        val baseUrl: String = ApiConfig.BASE_URL
        val incorrectUrl = "https://api.dicoding.com/"

        Assert.assertFalse(baseUrl == incorrectUrl)
    }
}
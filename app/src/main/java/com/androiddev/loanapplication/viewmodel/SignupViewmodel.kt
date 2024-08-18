package com.androiddev.loanapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androiddev.loanapplication.ApiConfig
import com.androiddev.loanapplication.model.CommonResponse
import com.androiddev.loanapplication.network.SigninRequest
import com.androiddev.loanapplication.network.SignupRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SigninViewmodel : ViewModel() {

    private val _signinData = MutableLiveData<CommonResponse>()
    val signinData: LiveData<CommonResponse> get() = _signinData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = _isError

    var username: String = ""
    var password: String = ""
    var mobile: String = ""

    var errorMessage: String = ""

    fun signin() {
        _isLoading.value = true
        _isError.value = false

        val client = ApiConfig.getApiService().signin(SigninRequest(username, password))

        // Send API request using Retrofit
        client.enqueue(object : Callback<CommonResponse> {

            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e("MYTAG", "response " + response.body()?.message)
                    _isLoading.value = false
                    _signinData.postValue(response.body())
                } else {
                    onError("Data Processing Error")
                }

            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                onError(t.message)
            }
        })
    }

    fun signup() {
        _isLoading.value = true
        _isError.value = false

        val client = ApiConfig.getApiService().signup(SignupRequest(username, password, mobile))

        // Send API request using Retrofit
        client.enqueue(object : Callback<CommonResponse> {

            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e("MYTAG", "response " + response.body()?.message)
                    _isLoading.value = false
                    _signinData.postValue(response.body())
                } else {
                    onError("Data Processing Error")
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                onError(t.message)
            }
        })
    }


    private fun onError(inputMessage: String?) {

        val message =
            if (inputMessage.isNullOrBlank() or inputMessage.isNullOrEmpty()) "Unknown Error"
            else inputMessage

        errorMessage = StringBuilder("ERROR: ")
            .append("$message some data may not displayed properly").toString()

        _isError.value = true
        _isLoading.value = false
    }

}
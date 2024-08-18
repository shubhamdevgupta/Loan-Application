package com.androiddev.loanapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androiddev.loanapplication.ApiConfig
import com.androiddev.loanapplication.model.CommonResponse
import com.androiddev.loanapplication.model.loanHistoryResponse
import com.androiddev.loanapplication.network.LoanRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoanViewmodel : ViewModel() {


    private val _loanData = MutableLiveData<CommonResponse>()
    val loanData: LiveData<CommonResponse> get() = _loanData


    private val _loanHistoryData = MutableLiveData<loanHistoryResponse>()
    val loanHistoryData: LiveData<loanHistoryResponse> get() = _loanHistoryData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading


    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = _isError

    var mobile: String = ""
    var loan_amount: String = ""
    var loan_duration: String = ""

    var errorMessage: String = ""

    fun applyLoan() {
        _isLoading.value = true
        _isError.value = false

        val client =
            ApiConfig.getApiService().applyLoan(
                LoanRequest(
                    mobile = mobile,
                    loan_amount = loan_amount,
                    loan_duration = loan_duration
                )
            )
        client.enqueue(object : Callback<CommonResponse> {

            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e("MYTAG", "response " + response.body()?.message)
                    _isLoading.value = false
                    _loanData.postValue(response.body())
                } else {
                    onError("Data Processing Error")
                }

            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                onError(t.message)
            }
        })
    }

    fun loanHistory() {
        _isLoading.value = true
        _isError.value = false

        val client =
            ApiConfig.getApiService().loanHistory(mobile)
        client.enqueue(object : Callback<loanHistoryResponse> {

            override fun onResponse(
                call: Call<loanHistoryResponse>,
                response: Response<loanHistoryResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e("MYTAG", "response " + response.body())
                    _isLoading.value = false
                    _loanHistoryData.postValue(response.body())
                } else {
                    onError("Data Processing Error")
                }
            }

            override fun onFailure(call: Call<loanHistoryResponse>, t: Throwable) {
                onError(t.message)
            }
        })
    }

    /*
        fun updateProfile() {
            _isLoading.value = true
            _isError.value = false

            val client =
                ApiConfig.getApiService().loanHistory(
                    LoanHistory(
                        mobile = mobile,
                    )
                )
            client.enqueue(object : Callback<CommonResponse> {

                override fun onResponse(
                    call: Call<CommonResponse>,
                    response: Response<CommonResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.e("MYTAG", "response " + response.body()?.message)
                        _isLoading.value = false
                        _loanData.postValue(response.body())
                    } else {
                        onError("Data Processing Error")
                    }

                }

                override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                    onError(t.message)
                }
            })

        }
    */

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
package com.androiddev.loanapplication.network

import com.androiddev.loanapplication.model.CommonResponse
import com.androiddev.loanapplication.model.loanHistoryResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiInterface {
    @POST("/sign_in")
    fun signin(@Body signinRequest: SigninRequest): Call<CommonResponse>

    @POST("/sign_up")
    fun signup(@Body signupRequest: SignupRequest): Call<CommonResponse>


    @POST("/apply_loan")
    fun applyLoan(@Body applyLoan: LoanRequest): Call<CommonResponse>


    @GET("/loan_history")
    fun loanHistory(@Query("mobile") mobile: String): Call<loanHistoryResponse>

    @POST("/update_profile")
    fun updateProfile(@Body updateProfile: UpdateProfile): Call<CommonResponse>

    @POST("/upload_user_data")
    fun uploadUserData(@Body uploadData: UploadUserData): Call<CommonResponse>

    @POST("/upload_photo")
    fun uploadPhoto(@Body uploadData: UploadPhoto): Call<CommonResponse>

    @GET("/admin/view_user_data")
    fun adminLogin(
        @Query("mobile") mobile: String,
        @Query("admin_id") adminId: String
    ): Call<CommonResponse>


}
package com.chris.thomson.midlandsriders.api


import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface API {

    @POST("logIn")
    fun LogIn(
            @Query("email") email: String,
            @Query("password") password: String): Call<ResponseBody>

    @POST("resetPassword")
    fun ResetPassword(
            @Query("email") email: String,
            @Query("password") password: String): Call<ResponseBody>
}
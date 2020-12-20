package com.chris.thomson.midlandsriders.Utils


import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface ApliList {
    @POST("midlands/v1/logIn")
    fun LogIn(
            @Query("email") email: String,
            @Query("password") password: String): Call<ResponseBody>
}
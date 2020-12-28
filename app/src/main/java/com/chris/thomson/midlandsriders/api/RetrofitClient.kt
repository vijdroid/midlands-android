package com.chris.thomson.midlandsriders.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

//    private val AUTH = "Basic "+ Base64.encodeToString("belalkhan:123456".toByteArray(), Base64.NO_WRAP)
val AUTH = "midlandsriders123!@#"

    private const val BASE_URL = "http://www.assurelive.com/midlands/v1/"

    private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()

                val requestBuilder = original.newBuilder()
                        .addHeader("Authorization", AUTH)
                        .method(original.method(), original.body())

                val request = requestBuilder.build()
                chain.proceed(request)
            }.build()

    val instance: API by lazy{
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

        retrofit.create(API::class.java)
    }

}
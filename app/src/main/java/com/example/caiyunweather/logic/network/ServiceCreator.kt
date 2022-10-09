package com.example.caiyunweather.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Retrofit构建器
object ServiceCreator {
    private const val BASE_URL = "https://api.caiyunapp.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(serviceClass: Class<T>) = retrofit.create(serviceClass)

    // 使用内联实现泛型实化
    inline fun <reified T> create() = create(T::class.java)
}
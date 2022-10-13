package com.example.caiyunweather.logic.network

import com.example.caiyunweather.CaiyunWeatherApplication
import com.example.caiyunweather.logic.model.DailyResponse
import com.example.caiyunweather.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {

    @GET("v2.5/${CaiyunWeatherApplication.TOKEN}/{lng},{lat}/realtime.json ")
    fun getRealtimeWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<RealtimeResponse>

    @GET("v2.5/${CaiyunWeatherApplication.TOKEN}/{lng},{lat}/daily.json ")
    fun getDailyWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<DailyResponse>
}
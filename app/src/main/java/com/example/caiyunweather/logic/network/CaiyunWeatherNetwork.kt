package com.example.caiyunweather.logic.network

import retrofit2.await

/**
 * 定义一个统一的网络数据源访问入口，对所有网络请求的API进行封装
 */
object CaiyunWeatherNetwork {

    // 用ServiceCreator创建一个PlaceService接口的动态代理对象
    private val placeService = ServiceCreator.create<PlaceService>()
    // 调用刚刚在PlaceService接口中定义的searchPlaces()方法，以发起搜索城市数据请求
    suspend fun searchPlaces(query: String) = placeService.searchPlace(query).await()
}
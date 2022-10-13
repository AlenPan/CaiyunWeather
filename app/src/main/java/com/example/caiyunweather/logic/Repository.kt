package com.example.caiyunweather.logic

import androidx.lifecycle.liveData
import com.example.caiyunweather.logic.dao.PlaceDao
import com.example.caiyunweather.logic.model.Place
import com.example.caiyunweather.logic.model.Weather
import com.example.caiyunweather.logic.network.CaiyunWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

/**
 * 仓库层的主要工作就是判断调用方请求的数据应该是从本地数据源中获取还是从网络数据源中获取，
 * 并将获得的数据返回给调用方。
 */
object Repository {

    /**
     *      liveData()函数是lifecycle-livedata-ktx库提供的一个非常强大且好用的功能，它可以自动构建并返回
     *      一个LiveData对象，然后在它的代码块中提供一个挂起函数的上下文。
     *      将liveData()函数的线程参数类型指定成了Dispatchers.IO，这样代码块中的所有代码就都运行在子线程中了。
     */
    fun searchPlace(query: String) = fire(Dispatchers.IO) {
        val placeResponse = CaiyunWeatherNetwork.searchPlaces(query)
        if (placeResponse.status == "ok") {
            val places = placeResponse.places
            Result.success(places)
        } else {
            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
        }
    }

    /**
     * 提供了一个refreshWeather()方法用来刷新天气信息，对获取实时天气信息和未来天气信息方法进行封装
     */
    fun refreshWeather(lat: String, lng: String) = fire(Dispatchers.IO) {
        // 并发执行获取实时天气信息和获取未来天气信息这两个请求
        coroutineScope {
            // 分别在两个async函数中发起网络请求，然后再分别调用它们的await()方法，
            // 就可以保证只有在两个网络请求都成功响应之后，才会进一步执行程序
            val deferredRealtime = async {
                CaiyunWeatherNetwork.searchRealtimeWeather(lat, lng)
            }
            val deferredDaily = async {
                CaiyunWeatherNetwork.searchDailyWeather(lat, lng)
            }
            val realtimeResponse = deferredRealtime.await()
            val dailyResponse = deferredDaily.await()
            if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                val realtime = realtimeResponse.result.realtime
                val daily = dailyResponse.result.daily
                Result.success(Weather(realtime, daily))
            } else {
                Result.failure(
                    RuntimeException(
                        "realtime response status is ${realtimeResponse.status}" +
                                "daily response status is ${dailyResponse.status}"
                    )
                )
            }

        }
    }


    /**
     * 封装每个网络请求接口可能会抛出的异常，使得只要进行一次try catch处理就行了
     */
    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure(e)
            }
            // emit()方法其实类似于调用LiveData的setValue()方法来通知数据变化，只不过这里我们无法直接取得返回的
            // LiveData对象，所以lifecycle-livedata-ktx库提供了这样一个替代方法。
            emit(result)
        }

    fun savePlace(place: Place) = PlaceDao.savePlace(place)

    fun getSavedPlace(): Place = PlaceDao.getSavedPlace()

    fun isPlaceSaved(): Boolean = PlaceDao.isPlaceSaved()
}
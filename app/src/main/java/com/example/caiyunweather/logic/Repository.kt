package com.example.caiyunweather.logic

import androidx.lifecycle.liveData
import com.example.caiyunweather.logic.model.Place
import com.example.caiyunweather.logic.network.CaiyunWeatherNetwork
import kotlinx.coroutines.Dispatchers

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
    fun searchPlace(query: String) = liveData(Dispatchers.IO) {
        val result = try {
            val placeResponse = CaiyunWeatherNetwork.searchPlaces(query)
            if (placeResponse.status == "ok") {
                val places = placeResponse.places
                Result.success(places)
            } else {
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        // emit()方法其实类似于调用LiveData的setValue()方法来通知数据变化，只不过这里我们无法直接取得返回的
        // LiveData对象，所以lifecycle-livedata-ktx库提供了这样一个替代方法。
        emit(result)
    }
}
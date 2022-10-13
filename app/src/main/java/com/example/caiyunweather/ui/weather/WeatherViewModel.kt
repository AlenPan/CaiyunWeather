package com.example.caiyunweather.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.caiyunweather.logic.Repository
import com.example.caiyunweather.logic.model.Location
import com.example.caiyunweather.logic.model.Weather

class WeatherViewModel: ViewModel() {

    private val locationLiveData = MutableLiveData<Location>()

    // 和界面相关的缓存数据，保证它们在手机屏幕发生旋转的时候不会丢失
    var locationLat = ""
    var locationLng = ""
    var placeName = ""

    // switchMap()方法会将Repository.refreshWeather()方法返回的LiveData对象转换成一个可观察的LiveData对象
    val weatherLiveData = Transformations.switchMap(locationLiveData) { location ->
        Repository.refreshWeather(location.lat, location.lng)
    }

    fun refreshWeather(lng: String, lat: String) {
        locationLiveData.value = Location(lat, lng)
    }
}
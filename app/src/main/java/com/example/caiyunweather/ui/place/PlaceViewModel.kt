package com.example.caiyunweather.ui.place

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.caiyunweather.logic.Repository
import com.example.caiyunweather.logic.model.Place

class PlaceViewModel: ViewModel() {

    // 对界面上显示的城市数据进行缓存
    val placeList = ArrayList<Place>()

    private val searchLiveData = MutableLiveData<String>()

    // switchMap()方法会将Repository.searchPlace()方法返回的LiveData对象转换成一个可观察的LiveData对象
    val placeLiveData = Transformations.switchMap(searchLiveData) { query ->
        // searchLiveData的数据一旦发生变化，观察searchLiveData的switchMap()方法就会执行，并且调用转换函数
        // 在转换函数中调用Repository.searchPlace()方法获取真正的用户数据，并返回一个LiveData对象
        Repository.searchPlace(query)
    }

    // 外部调用时，不会发起任何请求或者函数调用，而是将传入值赋值给searchLiveData
    fun searchPlace(query: String) {
        searchLiveData.value = query
    }

    fun savePlace(place: Place) = Repository.savePlace(place)

    fun getSavedPlace(): Place = Repository.getSavedPlace()

    fun isPlaceSaved(): Boolean = Repository.isPlaceSaved()
}
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

    val placeLiveData = Transformations.switchMap(searchLiveData) { query ->
        Repository.searchPlace(query)
    }

    fun searchPlace(query: String) {
        searchLiveData.value = query
    }
}
package com.example.caiyunweather.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.example.caiyunweather.CaiyunWeatherApplication
import com.example.caiyunweather.logic.model.Place
import com.google.gson.Gson

object PlaceDao {

    private fun sharedPreferences() = CaiyunWeatherApplication.context.getSharedPreferences(
        "caiyun_weather",
        Context.MODE_PRIVATE
    )

    fun savePlace(place: Place) {
        sharedPreferences().edit {
            putString("place", Gson().toJson(place))
        }
    }

    fun getSavedPlace(): Place {
        val placeJson = sharedPreferences().getString("place", "")
        return Gson().fromJson(placeJson, Place::class.java)
    }

    fun isPlaceSaved(): Boolean {
        return sharedPreferences().contains("place")
    }
}
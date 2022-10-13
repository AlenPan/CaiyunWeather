package com.example.caiyunweather.logic.model

import com.google.gson.annotations.SerializedName

data class PlaceResponse(val status: String, val places: List<Place>)

data class Place(
    val name: String, val location: Location,
    @SerializedName("formatted_address") val address: String
)

/**
 * lat: latitude 纬度
 * lng: longitude 经度
 */
data class Location(val lat: String, val lng: String)
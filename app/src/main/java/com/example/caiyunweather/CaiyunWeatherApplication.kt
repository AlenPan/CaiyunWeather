package com.example.caiyunweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class CaiyunWeatherApplication: Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        const val TOKEN = "gVBseJttGi76hoHO"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}
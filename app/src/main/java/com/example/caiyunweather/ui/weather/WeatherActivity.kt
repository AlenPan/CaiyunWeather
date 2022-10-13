package com.example.caiyunweather.ui.weather

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowInsetsController
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.caiyunweather.R
import com.example.caiyunweather.databinding.ActivityWeatherBinding
import com.example.caiyunweather.databinding.ForecastBinding
import com.example.caiyunweather.databinding.ItemForecastBinding
import com.example.caiyunweather.logic.model.Weather
import com.example.caiyunweather.logic.model.getSky
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherBinding

    val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 沉浸式
//        val controller = ViewCompat.getWindowInsetsController(binding.root)
//        controller?.show(WindowInsetsCompat.Type.systemBars())
//        controller?.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }
        window.statusBarColor = Color.TRANSPARENT


        if (viewModel.locationLng.isEmpty()) {
            viewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
        }
        if (viewModel.locationLat.isEmpty()) {
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        }
        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
        }

        viewModel.weatherLiveData.observe(this, Observer { result ->
            val weather = result.getOrNull()
            if (weather != null) {
                showWeatherInfo(weather)
            } else {
                Toast.makeText(this, "无法成功获取天气信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })

        viewModel.refreshWeather( viewModel.locationLat, viewModel.locationLng)
    }

    private fun showWeatherInfo(weather: Weather) {
        binding.run {
            includeNow.placeName.text = viewModel.placeName
            val realtime = weather.realtime
            val daily = weather.daily
            // 填充now.xml布局中的数据
            val currentTempText = "${realtime.temperature.toInt()} ℃"
            includeNow.currentTemp.text = currentTempText
            includeNow.currentSky.text = getSky(realtime.skycon).info
            val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
            includeNow.currentAQI.text = currentPM25Text
            includeNow.nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)
            // 填充forecast.xml布局中的数据
            includeForecast.forecastLayout.removeAllViews()
            val days = daily.skycon.size
            for (i in 0 until days) {
                val skycon = daily.skycon[i]
                val temperature = daily.temperature[i]
                val view = LayoutInflater.from(this@WeatherActivity).inflate(
                    R.layout.item_forecast,
                    includeForecast.forecastLayout, false
                )
                val viewBinding = ItemForecastBinding.bind(view)
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                viewBinding.dateInfo.text = simpleDateFormat.format(skycon.date)
                val sky = getSky(skycon.value)
                viewBinding.skyIcon.setImageResource(sky.icon)
                viewBinding.skyInfo.text = sky.info
                val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
                viewBinding.temperatureInfo.text = tempText
                includeForecast.forecastLayout.addView(view)
            }
            // 填充life_index.xml布局中的数据
            val lifeIndex = daily.lifeIndex
            includeLifeIndex.coldRiskText.text = lifeIndex.coldRisk[0].desc
            includeLifeIndex.dressingText.text = lifeIndex.dressing[0].desc
            includeLifeIndex.ultravioletText.text = lifeIndex.ultraviolet[0].desc
            includeLifeIndex.carWashingText.text = lifeIndex.carWashing[0].desc
            // 设置ScrollView变成可见状态
            weatherLayout.visibility = View.VISIBLE
        }

    }
}
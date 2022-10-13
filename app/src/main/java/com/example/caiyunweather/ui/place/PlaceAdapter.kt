package com.example.caiyunweather.ui.place

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.caiyunweather.databinding.ItemPlaceBinding
import com.example.caiyunweather.logic.model.Place
import com.example.caiyunweather.logic.model.Weather
import com.example.caiyunweather.ui.weather.WeatherActivity

class PlaceAdapter(private val fragment: PlaceFragment, private val placeList: List<Place>) :
    RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {

    inner class PlaceViewHolder(val binding: ItemPlaceBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = ItemPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val holder = PlaceViewHolder(binding)
        holder.binding.root.setOnClickListener {
            val position = holder.adapterPosition
            val place = placeList[position]
            val intent = Intent(parent.context, WeatherActivity::class.java).apply {
                putExtra("location_lng", place.location.lng)
                putExtra("location_lat", place.location.lat)
                putExtra("place_name", place.name)
            }
            // 记录选中的城市
            fragment.viewModel.savePlace(place)
            fragment.startActivity(intent)
//            fragment.activity?.finish()
        }
        return holder
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = placeList[position]
        holder.binding.let {
            it.placeName.text = place.name
            it.placeAddress.text = place.address
        }
    }

    override fun getItemCount() = placeList.size

}
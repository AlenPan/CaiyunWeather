package com.example.caiyunweather.ui.place

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.caiyunweather.R

open class BaseFragment<T: ViewBinding>: Fragment(R.layout.fragment_place) {

    protected var _binding: T? = null
    protected val binding get() = _binding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
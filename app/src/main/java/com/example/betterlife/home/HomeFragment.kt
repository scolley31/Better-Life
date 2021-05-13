package com.example.betterlife.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.betterlife.databinding.FragmentHomeBinding

class HomeFragment(): Fragment() {

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater)
        binding.viewpagerHome.adapter = HomeAdapter(childFragmentManager)
        binding.tabsHome.setupWithViewPager(binding.viewpagerHome)

        return binding.root
    }
}
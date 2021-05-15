package com.example.betterlife.timer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.betterlife.databinding.FragmentHomeBinding
import com.example.betterlife.databinding.FragmentTimerBinding
import com.example.betterlife.ext.getVmFactory
import com.example.betterlife.home.HomeAdapter

class TimerFragment(): Fragment() {

    lateinit var binding: FragmentTimerBinding

    private val viewModel by viewModels<TimerViewModel> { getVmFactory(TimerFragmentArgs.fromBundle(requireArguments()).planKey) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentTimerBinding.inflate(inflater)
        binding.viewModel = viewModel
        Log.d("test","argement = ${viewModel.plan.value}")
        binding.viewpagerTimer.adapter = TimeAdapter(childFragmentManager)
        binding.tabsTimer.setupWithViewPager(binding.viewpagerTimer)



        return binding.root
    }

}
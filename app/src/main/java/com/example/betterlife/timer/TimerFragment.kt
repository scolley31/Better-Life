package com.example.betterlife.timer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.betterlife.databinding.FragmentTimerBinding
import com.example.betterlife.ext.getVmFactory
import com.google.android.material.tabs.TabLayout

class TimerFragment(): Fragment() {

    lateinit var binding: FragmentTimerBinding
    private val viewModel by viewModels<TimerViewModel> { getVmFactory(TimerFragmentArgs.fromBundle(requireArguments()).planKey) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentTimerBinding.inflate(inflater)
        binding.viewModel = viewModel
        Log.d("test","argement = ${viewModel.plan.value}")

        viewModel.plan.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.viewpagerTimer.let {
                    binding.tabsTimer.setupWithViewPager(it)
                    it.adapter = TimerAdapter(childFragmentManager, viewModel.plan.value!!)
                    it.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabsTimer))
                }
            }
        })




        return binding.root
    }

}
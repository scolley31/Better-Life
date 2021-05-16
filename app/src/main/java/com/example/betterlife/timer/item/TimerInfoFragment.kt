package com.example.betterlife.timer.item

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.betterlife.data.Plan
import com.example.betterlife.databinding.FragmentTimerBinding
import com.example.betterlife.databinding.FragmentTimerInfoBinding
import com.example.betterlife.databinding.FragmentTimerItemBinding
import com.example.betterlife.ext.getVmFactory

class TimerInfoFragment(private val plan: Plan):Fragment() {

    lateinit var binding: FragmentTimerInfoBinding
    private val viewModel by viewModels<TimerInfoViewModel> { getVmFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentTimerInfoBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel._info.value = plan

        viewModel.info.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.d("test", "info = ${viewModel.info.value}")
//            binding.Timer.text = viewModel._timer.value!!.sumDaily!!.toLong().toString()
        })

        return binding.root
    }

}
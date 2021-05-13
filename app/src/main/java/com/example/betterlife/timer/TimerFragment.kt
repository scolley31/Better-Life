package com.example.betterlife.timer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.betterlife.databinding.FragmentHomeBinding
import com.example.betterlife.databinding.FragmentTimerBinding

class TimerFragment(): Fragment() {

    lateinit var binding: FragmentTimerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentTimerBinding.inflate(inflater)





        return binding.root
    }

}
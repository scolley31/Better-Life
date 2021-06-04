package com.example.betterlife.timer.team

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.betterlife.data.Plan
import com.example.betterlife.data.PlanForShow
import com.example.betterlife.databinding.FragmentTimerInfoBinding
import com.example.betterlife.databinding.FragmentTimerTeamBinding

class TimerTeamFragment(private val plan: PlanForShow?): Fragment() {

    lateinit var binding: FragmentTimerTeamBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentTimerTeamBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner


        return binding.root
    }

}
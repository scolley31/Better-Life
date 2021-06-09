package com.scolley.betterlife.timer.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.scolley.betterlife.data.Plan
import com.scolley.betterlife.databinding.FragmentTimerInfoBinding
import com.scolley.betterlife.databinding.FragmentTimerTeamBinding

class TimerTeamFragment(private val plan: Plan): Fragment() {

    lateinit var binding: FragmentTimerTeamBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentTimerTeamBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner


        return binding.root
    }

}
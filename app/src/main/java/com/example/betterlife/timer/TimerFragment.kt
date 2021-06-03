package com.example.betterlife.timer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.betterlife.R
import com.example.betterlife.data.PlanForShow
import com.example.betterlife.databinding.FragmentTimerBinding
import com.example.betterlife.ext.getVmFactory
import com.google.android.material.tabs.TabLayout


class TimerFragment(): Fragment() {

    lateinit var binding: FragmentTimerBinding

    private val viewModel by viewModels<TimerViewModel> { getVmFactory(TimerFragmentArgs.fromBundle(requireArguments()).planKey,TimerFragmentArgs.fromBundle(requireArguments()).planTeam) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentTimerBinding.inflate(inflater)

        binding.viewModel = viewModel
        Log.d("test", "plan = ${viewModel.plan.value}")

        val view1: View = layoutInflater.inflate(R.layout.customtab, null)
        view1.findViewById<View>(R.id.icon).setBackgroundResource(R.drawable._21_stopwatch)

        val view2: View = layoutInflater.inflate(R.layout.customtab, null)
        view2.findViewById<View>(R.id.icon).setBackgroundResource(R.drawable._38_presentation)

        viewModel.plan.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d("test", "planIntimer = ${viewModel.plan.value}")
                binding.viewpagerTimer.let {
                    binding.tabsTimer.setupWithViewPager(it)
                    it.adapter = TimerAdapter(childFragmentManager, viewModel.plan.value!!)
                    it.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabsTimer))

                    binding.tabsTimer.getTabAt(0)!!.setCustomView(view1)
                    binding.tabsTimer.getTabAt(1)!!.setCustomView(view2)

                }
            }
        })

        viewModel.planTeam.observe(viewLifecycleOwner, Observer {
            it?.let {

                Log.d("test", "planIntimer team = ${viewModel.planTeam.value}")


                }
            }
        )

        return binding.root
    }

}
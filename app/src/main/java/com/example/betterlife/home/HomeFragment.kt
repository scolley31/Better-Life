package com.example.betterlife.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.betterlife.databinding.FragmentHomeBinding
import com.example.betterlife.ext.getVmFactory
import com.example.betterlife.home.item.HomeDoneViewModel

class HomeFragment(): Fragment() {

    lateinit var binding: FragmentHomeBinding

    private val viewModel by viewModels<HomeViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.viewpagerHome.adapter = HomeAdapter(childFragmentManager)
        binding.tabsHome.setupWithViewPager(binding.viewpagerHome)

        viewModel.plans.observe(viewLifecycleOwner, Observer {
            it?.let{
                viewModel.checkTodayDoneNumber()
                Log.d("test","plans = ${viewModel.plans.value}")
                Log.d("test","taskDoneNumber = ${viewModel.taskDoneNumber.value}")
            }
        }
        )

        return binding.root
    }
}
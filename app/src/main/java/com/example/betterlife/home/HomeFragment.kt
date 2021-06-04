package com.example.betterlife.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.betterlife.NavigationDirections
import com.example.betterlife.databinding.FragmentHomeBinding
import com.example.betterlife.ext.getVmFactory

class HomeFragment(): Fragment() {

    lateinit var binding: FragmentHomeBinding

    private val viewModel by viewModels<HomeViewModel> { getVmFactory(HomeFragmentArgs.fromBundle(requireArguments()).userIDKey) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel


        viewModel.plans.observe(viewLifecycleOwner, Observer {
            it?.let{
//                viewModel.getCompleted()
            }
        }
        )

        viewModel.navigateToAddTask.observe(viewLifecycleOwner, Observer {
            Log.i("test","navigateToAddTask = ${viewModel.navigateToAddTask.value}")
            it?.let {
                findNavController().navigate(NavigationDirections.actionGlobalAddTaskFragment())
            }
        }
        )

        viewModel.user.observe(viewLifecycleOwner, Observer {
            it?.let{
//                Log.d("user","user = ${viewModel.user.value}")
                binding.viewpagerHome.adapter = HomeAdapter(childFragmentManager, viewModel.user.value!!)
                binding.tabsHome.setupWithViewPager(binding.viewpagerHome)
            }
        }
        )

        return binding.root
    }
}
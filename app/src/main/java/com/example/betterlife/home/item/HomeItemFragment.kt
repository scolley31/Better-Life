package com.example.betterlife.home.item

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.betterlife.databinding.FragmentHomeItemBinding
import com.example.betterlife.ext.getVmFactory
import com.example.betterlife.home.PlanStatus
import com.example.betterlife.other.OtherViewModel

class HomeItemFragment(private val planStatus: PlanStatus) : Fragment() {

    private val viewModel by viewModels<HomeItemViewModel> { getVmFactory() }

    lateinit var binding: FragmentHomeItemBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentHomeItemBinding.inflate(inflater, container, false)

        binding.RecyclerHome.adapter = HomeItemAdapter(viewModel)

        viewModel.plan.observe(viewLifecycleOwner, Observer {
                Log.i("test","plan = ${viewModel.plan.value}")
            }
        )


        return binding.root
    }


}
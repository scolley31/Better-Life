package com.example.betterlife.home.item

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.betterlife.NavigationDirections
import com.example.betterlife.data.Plan
import com.example.betterlife.databinding.FragmentHomeItemBinding
import com.example.betterlife.ext.getVmFactory
import com.example.betterlife.home.PlanStatus

class HomeItemFragment(private val planStatus: PlanStatus) : Fragment() {

    private val viewModel by viewModels<HomeItemViewModel> { getVmFactory() }

    lateinit var binding: FragmentHomeItemBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentHomeItemBinding.inflate(inflater, container, false)

        viewModel.plans.observe(viewLifecycleOwner, Observer {
            it?.let{
                Log.i("test","planInFragment = ${viewModel.plans.value}")
            }

            }
        )

        viewModel.navigateToTimer.observe(viewLifecycleOwner, Observer {
            Log.i("test","navigateToTimer = ${viewModel.navigateToTimer.value}")
            it?.let {
                findNavController().navigate(NavigationDirections.actionGlobalTimerFragment(it))
                viewModel.deleteNavigateTimer()
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

        viewModel.plans.observe(viewLifecycleOwner, Observer {
            Log.i("test","plans = ${viewModel.plans.value}")
//            viewModel.getCompleted()

        }
        )

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.RecyclerHome.layoutManager = LinearLayoutManager(context)
        binding.RecyclerHome.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        binding.RecyclerHome.adapter = HomeItemAdapter(viewModel, HomeItemAdapter.OnClickListener{
            viewModel.navigateTimer(it)
            Log.d("test","plan = $it")
        })


        return binding.root
    }


}
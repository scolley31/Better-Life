package com.scolley.betterlife.home.item

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.scolley.betterlife.NavigationDirections
import com.scolley.betterlife.data.User
import com.scolley.betterlife.databinding.FragmentHomeItemBinding
import com.scolley.betterlife.ext.getVmFactory


class HomeItemFragment(private val user: User) : Fragment() {

    private val viewModel by viewModels<HomeItemViewModel> { getVmFactory() }

    lateinit var binding: FragmentHomeItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        viewModel._user.value = user

        super.onCreate(savedInstanceState)
    }

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
                findNavController().navigate(NavigationDirections.actionGlobalTimerFragment(it,null))
                viewModel.deleteNavigateTimer()
            }
        }
        )

        viewModel.user.observe(viewLifecycleOwner, Observer {
            Log.i("test","user = ${viewModel.user.value}")
        }
        )


        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.RecyclerHome.layoutManager = LinearLayoutManager(context)
//        binding.RecyclerHome.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        binding.RecyclerHome.adapter = HomeItemAdapter(viewModel, HomeItemAdapter.OnClickListener{
            viewModel.navigateTimer(it)
//            Log.d("test","plan = $it")
        })


        return binding.root
    }


}
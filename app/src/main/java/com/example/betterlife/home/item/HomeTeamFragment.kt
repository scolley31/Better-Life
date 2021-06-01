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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.betterlife.NavigationDirections
import com.example.betterlife.data.User
import com.example.betterlife.databinding.FragmentHomeItemBinding
import com.example.betterlife.databinding.FragmentHomeTeamBinding
import com.example.betterlife.ext.getVmFactory

class HomeTeamFragment(private val user: User) : Fragment() {

    private val viewModel by viewModels<HomeTeamViewModel> { getVmFactory() }

    lateinit var binding: FragmentHomeTeamBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        viewModel._user.value = user

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        binding = FragmentHomeTeamBinding.inflate(inflater, container, false)

        viewModel.plans.observe(viewLifecycleOwner, Observer {
            it?.let{
                Log.i("HomeTeamFragment","planInFragment = ${viewModel.plans.value}")
                viewModel.getCompletedAndGroup()
                viewModel.setShowData()
            }
        }
        )

        viewModel.plansForShow.observe(viewLifecycleOwner, Observer {
            it?.let{
                Log.i("HomeTeamFragment","plansForShow = ${viewModel.plansForShow.value}")
            }
        }
        )

        viewModel.groups.observe(viewLifecycleOwner, Observer {
            it?.let{
            }
        }
        )

        viewModel.partnerPlanCompleted.observe(viewLifecycleOwner, Observer {
            it?.let{
                Log.i("HomeTeamFragment","partnerPlanCompleted = ${viewModel.partnerPlanCompleted.value}")
            }
        }
        )

        viewModel.navigateToTimer.observe(viewLifecycleOwner, Observer {
            Log.i("HomeTeamFragment","navigateToTimer = ${viewModel.navigateToTimer.value}")
            it?.let {
                findNavController().navigate(NavigationDirections.actionGlobalTimerFragment(it))
                viewModel.deleteNavigateTimer()
            }
        }
        )

        viewModel.user.observe(viewLifecycleOwner, Observer {
            Log.i("HomeTeamFragment","user = ${viewModel.user.value}")
        }
        )


        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.RecyclerHome.layoutManager = LinearLayoutManager(context)
//        binding.RecyclerHome.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        binding.RecyclerHome.adapter = HomeTeamAdapter(viewModel, HomeTeamAdapter.OnClickListener{
//            viewModel.navigateTimer(it)
//            Log.d("test","plan = $it")
        })


        return binding.root
    }


}
package com.scolley.betterlife.other

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.scolley.betterlife.databinding.FragmentOtherBinding
import com.scolley.betterlife.ext.getVmFactory

class OtherFragment : Fragment() {

    private val viewModel by viewModels<OtherViewModel> { getVmFactory() }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentOtherBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.RecyclerOther.layoutManager = GridLayoutManager(context, 2)
        binding.RecyclerOther.adapter = OtherAdapter(viewModel, OtherAdapter.OnClickListener {
            Log.d("test", "otherPlan = $it")
        })

        viewModel.otherPlan.observe(viewLifecycleOwner, Observer {
            Log.i("test", "otherPlan = ${viewModel.otherPlan.value}")
        }
        )

        viewModel.category.observe(viewLifecycleOwner, Observer {
            Log.i("test", "category = ${viewModel.category.value}")
            it?.let {
                viewModel.getOtherSelectedPlanResult(it)
            }
        }
        )

        return binding.root
    }

}
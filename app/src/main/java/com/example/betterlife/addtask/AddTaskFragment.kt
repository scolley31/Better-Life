package com.example.betterlife.addtask

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.betterlife.NavigationDirections
import com.example.betterlife.R
import com.example.betterlife.databinding.DialogAddtaskBinding
import com.example.betterlife.ext.getVmFactory
import com.example.betterlife.home.item.HomeItemViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddTaskFragment(): AppCompatDialogFragment() {

    lateinit var binding:DialogAddtaskBinding

    private val viewModel by viewModels<AddTaskViewModel> { getVmFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.LoginDialog)
    }


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = DialogAddtaskBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val spinner: Spinner = binding.spinnerGoalCategory

        ArrayAdapter.createFromResource(
                this.requireContext(),
                R.array.category_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        binding.spinnerGoalCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                viewModel.category.value = binding.spinnerGoalCategory.selectedItem.toString()
            }
        }

        viewModel.navigateToHome.observe(viewLifecycleOwner, Observer {
            Log.i("test","navigateToHome = ${viewModel.navigateToHome.value}")
            it?.let {
                findNavController().navigate(NavigationDirections.actionGlobalHomeFragment(
                        FirebaseAuth.getInstance().currentUser!!.uid
                ))
            }
        }
        )

        viewModel.name.observe(viewLifecycleOwner, Observer {
            Log.i("test","name = ${viewModel.name.value}")
        }
        )

        viewModel.category.observe(viewLifecycleOwner, Observer {
            Log.i("test","category = ${viewModel.category.value}")
        }
        )

        viewModel.target.observe(viewLifecycleOwner, Observer {
            Log.i("test","target = ${viewModel.target.value}")
        }
        )

        viewModel.dailyTarget.observe(viewLifecycleOwner, Observer {
            Log.i("test","dailyTarget = ${viewModel.dailyTarget.value}")
        }
        )


        return binding.root
    }

}
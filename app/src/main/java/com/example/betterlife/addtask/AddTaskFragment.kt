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
import java.text.SimpleDateFormat

class AddTaskFragment(): AppCompatDialogFragment() {

    lateinit var binding:DialogAddtaskBinding

    lateinit var adapter:ArrayAdapter<String>

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

//        val colors = arrayOf(
//                "Red","Green","Blue","Maroon","Magenta",
//                "Gold","GreenYellow"
//        )

        viewModel.userName.observe (viewLifecycleOwner, Observer {
            Log.i("test", "userName = ${viewModel.userName.value}")
            it?.let {
                // Initialize a new array adapter object
                adapter = ArrayAdapter<String>(
                        requireContext(), // Context
                        android.R.layout.simple_dropdown_item_1line, // Layout
                        viewModel.userName.value as MutableList<String> // Array
                )
                binding.autoCompleteTextView.setAdapter(adapter)
            }
        }
        )

        binding.autoCompleteTextView.onItemClickListener = AdapterView.OnItemClickListener{
            parent,view,position,id->
            val selectedItem = parent.getItemAtPosition(position).toString()
            // Display the clicked item using toast
            Toast.makeText(requireContext(),"Selected : $selectedItem",Toast.LENGTH_SHORT).show()
        }

        binding.autoCompleteTextView.setOnDismissListener {
            Toast.makeText(requireContext(),"Suggestion closed.",Toast.LENGTH_SHORT).show()
        }

        // Set a focus change listener for auto complete text view
        binding.autoCompleteTextView.onFocusChangeListener = View.OnFocusChangeListener{
            view, b ->
            if(b){
                // Display the suggestion dropdown on focus
                binding.autoCompleteTextView.showDropDown()
            }
        }


        binding.isGroup.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                viewModel.isGroup.value = true
                binding.autoCompleteTextView.visibility = View.VISIBLE
            } else {
                viewModel.isGroup.value = false
                binding.autoCompleteTextView.visibility = View.INVISIBLE
                viewModel.partner.value = ""
            }
        }

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

        binding.endDate.apply {
            minDate = System.currentTimeMillis()
            setOnDateChangedListener { _, year, month, date ->
                val format = SimpleDateFormat(getString(R.string.diary_record_date))
                viewModel.dueDate.value = format.parse("$date/${month + 1}/$year").time
            }
        }

        viewModel.selectedTypeRadio.observe (viewLifecycleOwner, Observer {
            Log.i("test", "selectedTypeRadio = ${viewModel.selectedTypeRadio.value}")
            it?.let {
                when (viewModel.selectedTypeRadio.value) {
                    R.id.radio_enddate -> {
                        binding.constraintTotalTimeST.visibility = View.INVISIBLE
                        binding.endDate.visibility = View.VISIBLE
                        viewModel.target.value = 0
                    }
                    R.id.radio_totaltime -> {
                        binding.constraintTotalTimeST.visibility = View.VISIBLE
                        binding.endDate.visibility = View.INVISIBLE
                        viewModel.dueDate.value = -1
                    }
                    else -> ""
                }
            }
        }
        )

        viewModel.navigateToHome.observe(viewLifecycleOwner, Observer {
            Log.i("test","navigateToHome = ${viewModel.navigateToHome.value}")
            it?.let {
                findNavController().navigate(NavigationDirections.actionGlobalHomeFragment(
                        FirebaseAuth.getInstance().currentUser!!.uid
                ))
            }
        }
        )

        viewModel.dueDate.observe(viewLifecycleOwner, Observer {
            Log.i("test","dueDate = ${viewModel.dueDate.value}")
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

        viewModel.partner.observe(viewLifecycleOwner, Observer {
            Log.i("test","partner = ${viewModel.partner.value}")
        }
        )

        viewModel.isGroup.observe(viewLifecycleOwner, Observer {
            Log.i("test","isGroup = ${viewModel.isGroup.value}")
        }
        )

        viewModel.users.observe(viewLifecycleOwner, Observer {
            Log.i("test","users = ${viewModel.users.value}")
        }
        )


        return binding.root
    }

}
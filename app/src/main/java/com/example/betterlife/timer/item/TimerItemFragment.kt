package com.example.betterlife.timer.item

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.betterlife.NavigationDirections
import com.example.betterlife.data.Plan
import com.example.betterlife.databinding.FragmentTimerItemBinding
import com.example.betterlife.ext.getVmFactory
import com.example.betterlife.util.PrefUtil
import com.google.firebase.auth.FirebaseAuth

class TimerItemFragment(private val plan: Plan) : Fragment() {

    private val viewModel by viewModels<TimerItemViewModel> { getVmFactory() }
    lateinit var binding: FragmentTimerItemBinding
    private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {

        viewModel._timer.value = plan
        viewModel.dailyTaskRemained.value = plan.dailyTarget.times(60)
        viewModel.dailyTaskTarget.value = plan.dailyTarget.times(60)

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentTimerItemBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.timer.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.d("test", "timer = ${viewModel.timer.value}")
        })

        viewModel.leaveTimer.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                if (it) findNavController().popBackStack()
            }
        })

        viewModel.navigateToHome.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.actionGlobalHomeFragment(
                        FirebaseAuth.getInstance().currentUser!!.uid
                ))
            }
        })

        viewModel.dailyTaskRemained.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.d("test", "dailyTaskRemained = ${viewModel.dailyTaskRemained.value}")
        })

        viewModel.dailyTaskTarget.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.d("test", "dailyTaskTarget = ${viewModel.dailyTaskTarget.value}")
        })

        viewModel.timeStatus.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.d("test", "timeStatus = ${viewModel.timeStatus.value}")
        })

        viewModel.completed.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.d("test", "completed = ${viewModel.completed.value}")
        })


        binding.begin.setOnClickListener {
            viewModel.timeStatus.value=  TimerStatus.Running
            startTimer()
            updateButtons()
        }

        binding.stop.setOnClickListener {
            viewModel.timeStatus.value =  TimerStatus.Stopped
            timer.cancel()
            updateButtons()
        }

        updateCountdownUI()

        return binding.root
    }

    private fun updateButtons(){
        when (viewModel.timeStatus.value) {
            TimerStatus.Running ->{
                binding.begin.isEnabled = false
                binding.stop.isEnabled = true

            }
            TimerStatus.Stopped -> {
                binding.begin.isEnabled = true
                binding.stop.isEnabled = false

            }
        }
    }

    private fun startTimer(){
        viewModel.timeStatus.value = TimerStatus.Running
        timer = object : CountDownTimer(viewModel.dailyTaskRemained.value?.times(1000)!!.toLong(), 1000) {
            override fun onFinish() = onTimerFinished()

            override fun onTick(millisUntilFinished: Long) {
                viewModel.dailyTaskRemained.value = (millisUntilFinished / 1000) .toInt()
                updateCountdownUI()
            }
        }.start()
    }

    private fun onTimerFinished(){
        viewModel.timeStatus.value = TimerStatus.Stopped
        PrefUtil.setSecondsRemaining(viewModel._timer.value!!.dailyTarget!!.toLong(), this.requireContext())
        viewModel.dailyTaskRemained.value = viewModel._timer.value!!.dailyTarget!!
        viewModel.completed.value = true
        updateButtons()
        updateCountdownUI()
    }

    private fun updateCountdownUI(){
        val minutesUntilFinished = viewModel.dailyTaskRemained.value?.div(60)
        val secondsInMinuteUntilFinished = minutesUntilFinished?.times(60)?.let { viewModel.dailyTaskRemained.value?.minus(it) }
        val secondsStr = secondsInMinuteUntilFinished.toString()
        binding.Timer.text = "$minutesUntilFinished:${if (secondsStr.length == 2) secondsStr else "0" + secondsStr}"
    }

}
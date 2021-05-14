package com.example.betterlife.timer.item

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.betterlife.databinding.FragmentHomeItemBinding
import com.example.betterlife.databinding.FragmentTimerItemBinding
import com.example.betterlife.ext.getVmFactory
import com.example.betterlife.home.PlanStatus
import com.example.betterlife.home.item.HomeItemViewModel
import com.example.betterlife.timer.TimerPage
import com.example.betterlife.util.PrefUtil
import java.util.*

class TimerItemFragment(private val timerPage: TimerPage) : Fragment() {

    private val viewModel by viewModels<TimerItemViewModel> { getVmFactory() }
    lateinit var binding: FragmentTimerItemBinding
    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds: Long = 1000
    private var timerStatus = TimerStatus.Stopped
    private var secondsRemaining: Long = 10

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentTimerItemBinding.inflate(inflater, container, false)

        binding.begin.setOnClickListener {
            timerStatus=  TimerStatus.Running
            startTimer()
            updateButtons()
        }

        binding.stop.setOnClickListener {
            timerStatus=  TimerStatus.Stopped
            timer.cancel()
            updateButtons()
        }

        binding.restart.setOnClickListener {
            timer.cancel()
            onTimerFinished()
        }

        return binding.root
    }

    private fun updateButtons(){
        when (timerStatus) {
            TimerStatus.Running ->{
                binding.begin.isEnabled = false
                binding.stop.isEnabled = true
                binding.restart.isEnabled = true
            }
            TimerStatus.Stopped -> {
                binding.begin.isEnabled = true
                binding.stop.isEnabled = false
                binding.restart.isEnabled = false
            }
            TimerStatus.Paused -> {
                binding.begin.isEnabled = true
                binding.stop.isEnabled = false
                binding.restart.isEnabled = true
            }
        }
    }

    private fun startTimer(){
        timerStatus = TimerStatus.Running
        timer = object : CountDownTimer(secondsRemaining * 1000, 1000) {
            override fun onFinish() = onTimerFinished()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateCountdownUI()
            }
        }.start()
    }

    private fun onTimerFinished(){
        timerStatus = TimerStatus.Stopped
        PrefUtil.setSecondsRemaining(timerLengthSeconds, this.requireContext())
        secondsRemaining = timerLengthSeconds
        updateButtons()
        updateCountdownUI()
    }

    private fun updateCountdownUI(){
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        binding.Timer.text = "$minutesUntilFinished:${if (secondsStr.length == 2) secondsStr else "0" + secondsStr}"
    }

}
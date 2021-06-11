package com.scolley.betterlife.timer.item

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.scolley.betterlife.NavigationDirections
import com.scolley.betterlife.R
import com.scolley.betterlife.data.Plan
import com.scolley.betterlife.databinding.FragmentTimerItemBinding
import com.scolley.betterlife.ext.getVmFactory
import com.scolley.betterlife.util.PrefUtil
import com.google.firebase.auth.FirebaseAuth
import com.scolley.betterlife.timer.TimerExpiredReceiver
import com.scolley.betterlife.util.NotificationUtil
import java.util.*

class TimerItemFragment(private val plan: Plan) : Fragment() {

    private val viewModel by viewModels<TimerItemViewModel> { getVmFactory() }
    lateinit var binding: FragmentTimerItemBinding
    private lateinit var timer: CountDownTimer
    lateinit var runnable: Runnable
    private var handler = Handler()
    private var timerLengthSeconds: Long = 0

    companion object {

        fun setAlarm(context: Context, nowSeconds: Long, secondsRemaining: Long): Long{
            val wakeUpTime = (nowSeconds + secondsRemaining) * 1000
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeUpTime, pendingIntent)
            PrefUtil.setAlarmSetTime(nowSeconds, context)
            return wakeUpTime
        }

        fun removeAlarm(context: Context){
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
            PrefUtil.setAlarmSetTime(0, context)
        }

        val nowSeconds: Long
            get() = Calendar.getInstance().timeInMillis / 1000
    }

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
//                if (it) findNavController().popBackStack()
                findNavController().navigate(NavigationDirections.actionGlobalHomeFragment(
                        FirebaseAuth.getInstance().currentUser!!.uid
                ))
            }
        })

        viewModel.navigateToHome.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.actionGlobalHomeFragment(
                        FirebaseAuth.getInstance().currentUser!!.uid
                ))
            }
        })

        viewModel.navigateToHome.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.d("test", "navigateToHome = ${viewModel.navigateToHome.value}")
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

        viewModel.selectedTypeRadio.observe (viewLifecycleOwner, Observer {
            it?.let {
                when (viewModel.selectedTypeRadio.value) {
                    R.id.radio_count_target -> {
                        viewModel.dailyTaskRemained.value = plan.dailyTarget.times(60)
                        updateCountdownUI()
                    }
                    R.id.radio_count_noLimit -> {
                        viewModel.dailyTaskRemained.value = 0
                        updateCountdownUI()
                    }
                    else -> {
                        viewModel.dailyTaskRemained.value = plan.dailyTarget.times(60)
                        updateCountdownUI()
                    }
                }
            }
        }
        )


        binding.begin.setOnClickListener {
        viewModel.timeStatus.value=  TimerStatus.Running
            when(viewModel.selectedTypeRadio.value) {
                R.id.radio_count_target -> startTimer()
                R.id.radio_count_noLimit -> timeNumberGo()
            }

            updateButtons()
        }

        binding.stop.setOnClickListener {
            viewModel.timeStatus.value =  TimerStatus.Stopped
            when(viewModel.selectedTypeRadio.value) {
                R.id.radio_count_target ->  timer.cancel()
                R.id.radio_count_noLimit -> endTimming()
            }
            updateButtons()
        }
        updateCountdownUI()

        return binding.root
    }

    override fun onResume() {
        super.onResume()



        initTimer()
        removeAlarm(requireContext())
        NotificationUtil.hideTimerNotification(requireContext())
    }

    override fun onPause() {
        super.onPause()

        if (viewModel.timeStatus.value == TimerStatus.Running){
            when(viewModel.selectedTypeRadio.value) {
                R.id.radio_count_target -> timer.cancel()
                R.id.radio_count_noLimit -> endTimming()
            }
            val wakeUpTime = setAlarm(requireContext(), nowSeconds, viewModel.dailyTaskRemained.value!!.toLong())
            val bundle = Bundle()
            bundle.putParcelable("planKey",viewModel.timer.value)
            bundle.putParcelable("planTeam",null)
            Log.d("planKey","planKey = $bundle")
            NotificationUtil.showTimerRunning(requireContext(), wakeUpTime, bundle )
        }
        else if (viewModel.timeStatus.value == TimerStatus.Stopped){
            NotificationUtil.showTimerPaused(requireContext())
        }

        PrefUtil.setPreviousTimerLengthSeconds(timerLengthSeconds, requireContext())
        PrefUtil.setSecondsRemaining(viewModel.dailyTaskRemained.value!!.toLong(), requireContext())
        PrefUtil.setTimerState(viewModel.timeStatus.value!!, requireContext())
    }

    private fun initTimer(){
        viewModel.timeStatus.value = PrefUtil.getTimerState(requireContext())

        //we don't want to change the length of the timer which is already running
        //if the length was changed in settings while it was backgrounded
//        if (viewModel.timeStatus.value == TimerStatus.Stopped)
//            setNewTimerLength()
//        else
//            setPreviousTimerLength()

//        viewModel.dailyTaskRemained.value = if (viewModel.timeStatus.value == TimerStatus.Running)
//            PrefUtil.getSecondsRemaining(requireContext()).toInt()
//        else
//            timerLengthSeconds.toInt()

        val alarmSetTime = PrefUtil.getAlarmSetTime(requireContext())
        if (alarmSetTime > 0)
            when(viewModel.selectedTypeRadio.value) {
                R.id.radio_count_target ->  viewModel.dailyTaskRemained.value = (viewModel.dailyTaskRemained.value!! - (nowSeconds - alarmSetTime) ).toInt()
                R.id.radio_count_noLimit -> viewModel.dailyTaskRemained.value = (viewModel.dailyTaskRemained.value!! + (nowSeconds - alarmSetTime) ).toInt()
            }


        if (viewModel.dailyTaskRemained.value!! <= 0)
            when(viewModel.selectedTypeRadio.value) {
                R.id.radio_count_target ->  onTimerFinished()
            }
        else if (viewModel.timeStatus.value == TimerStatus.Running)
            when(viewModel.selectedTypeRadio.value) {
                R.id.radio_count_target ->  startTimer()
                R.id.radio_count_noLimit -> timeNumberGo()
            }

        updateButtons()
        updateCountdownUI()
    }

    private fun setNewTimerLength(){
        val lengthInMinutes = PrefUtil.getTimerLength(requireContext())
        timerLengthSeconds = (lengthInMinutes * 60L)
        binding.TimerProgressBar.max = timerLengthSeconds.toInt()
    }

    private fun setPreviousTimerLength(){
        timerLengthSeconds = PrefUtil.getPreviousTimerLengthSeconds(requireContext())
        binding.TimerProgressBar.max = timerLengthSeconds.toInt()
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


    fun timeNumberGo() {
        runnable = Runnable {
            viewModel.dailyTaskRemained.value = viewModel.dailyTaskRemained.value?.plus(1)

            handler.postDelayed(runnable, 1000)
            updateCountdownUI()
        }
        handler.postDelayed(runnable, 1000)
    }

    fun endTimming() {

        handler.removeCallbacks(runnable)
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
        PrefUtil.setSecondsRemaining(viewModel._timer.value!!.dailyTarget.toLong(), this.requireContext())
        viewModel.dailyTaskRemained.value = viewModel._timer.value!!.dailyTarget
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
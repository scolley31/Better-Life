package com.scolley.betterlife.timer.team

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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.scolley.betterlife.NavigationDirections
import com.scolley.betterlife.R
import com.scolley.betterlife.data.PlanForShow
import com.scolley.betterlife.databinding.FragmentTimeTeamItemBinding
import com.scolley.betterlife.ext.getVmFactory
import com.scolley.betterlife.timer.item.TimerStatus
import com.scolley.betterlife.util.PrefUtil
import com.google.firebase.auth.FirebaseAuth
import com.scolley.betterlife.timer.TimerExpiredReceiver
import com.scolley.betterlife.timer.item.TimerItemFragment
import com.scolley.betterlife.util.NotificationUtil
import java.util.*

class TimerTeamItemFragment(private val plan: PlanForShow?) : Fragment() {

    private val viewModel by viewModels<TimerTeamItemViewModel> { getVmFactory() }
    lateinit var binding: FragmentTimeTeamItemBinding
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

        if (plan!!.selectedTypeRadio != null) {
            viewModel.selectedTypeRadio.value = plan.selectedTypeRadio
        }

        viewModel.dailyTaskTarget.value = plan.dailyTarget.times(60)

        NotificationUtil.planTeam = plan

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentTimeTeamItemBinding.inflate(inflater, container, false)
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
                findNavController().navigate(
                    NavigationDirections.actionGlobalHomeFragment(
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

        viewModel.dailyTaskRemained.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.d("test", "dailyTaskRemained = ${viewModel.dailyTaskRemained.value}")
            Log.d("test", "timer = ${viewModel.timer.value}")
            it?.let{

                when(viewModel.selectedTypeRadio.value) {
                    R.id.radio_count_target ->  viewModel._timer.value!!.dailyRemainTime = it
                    R.id.radio_count_noLimit -> viewModel._timer.value!!.dailyCountTime = it
                }
            }
        })

        viewModel.timeStatus.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.d("test", "timeStatus = ${viewModel.timeStatus.value}")
        })

        viewModel.completed.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.d("test", "completed = ${viewModel.completed.value}")
        })

        viewModel.selectedTypeRadio.observe (viewLifecycleOwner, Observer {
            it?.let {

                viewModel._timer.value!!.selectedTypeRadio = it

                when (viewModel.selectedTypeRadio.value) {
                    R.id.radio_count_target -> {
                        if (viewModel._timer.value!!.dailyRemainTime != 0) {
                            viewModel.dailyTaskRemained.value = plan!!.dailyRemainTime
                        } else {
                            viewModel.dailyTaskRemained.value = plan!!.dailyTarget.times(60)
                        }
//                        viewModel.dailyTaskRemained.value = plan.dailyTarget.times(60)
                        updateCountdownUI()
                    }
                    R.id.radio_count_noLimit -> {

                        if (viewModel._timer.value!!.dailyCountTime != 0) {
                            viewModel.dailyTaskRemained.value = plan!!.dailyCountTime
                        } else {
                            viewModel.dailyTaskRemained.value = 0
                        }

                        updateCountdownUI()
                    }
                    else -> {
                        viewModel.dailyTaskRemained.value = plan!!.dailyTarget.times(60)
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
        TimerTeamItemFragment.removeAlarm(requireContext())
        NotificationUtil.hideTimerNotification(requireContext())
    }

    override fun onPause() {

        if (!viewModel._timer.value!!.todayDone) {
            val bundle = Bundle()
            bundle.putParcelable("planKey", null)
            bundle.putParcelable("planTeam", viewModel.timer.value)
            Log.d("planKey", "planKey = $bundle")

            NotificationUtil.selectedTypeRadio = viewModel.selectedTypeRadio.value!!
            Log.d("NotificationUtil.selectedTypeRadio", "NotificationUtil.selectedTypeRadio = ${NotificationUtil.selectedTypeRadio}")


            if (viewModel.timeStatus.value == TimerStatus.Running) {
                when (viewModel.selectedTypeRadio.value) {
                    R.id.radio_count_target -> {
                        timer.cancel()
                        val wakeUpTime = TimerTeamItemFragment.setAlarm(requireContext(), TimerTeamItemFragment.nowSeconds, viewModel.dailyTaskRemained.value!!.toLong())
                        NotificationUtil.showTimerRunning(requireContext(), wakeUpTime, bundle)
                    }
                    R.id.radio_count_noLimit -> {
                        endTimming()
                        val wakeUpTime = TimerTeamItemFragment.setAlarm(requireContext(), TimerTeamItemFragment.nowSeconds, viewModel.dailyTaskRemained.value!!.toLong())
                        NotificationUtil.showTimerRunningNoLimit(requireContext(), bundle)
                    }
                }

            } else if (viewModel.timeStatus.value == TimerStatus.Stopped) {
                NotificationUtil.showTimerPaused(requireContext(), bundle)
            }

            PrefUtil.setPreviousTimerLengthSeconds(timerLengthSeconds, requireContext())
            PrefUtil.setSecondsRemaining(viewModel.dailyTaskRemained.value!!.toLong(), requireContext())
            PrefUtil.setTimerState(viewModel.timeStatus.value!!, requireContext())
//            PrefUtil.setSelectedTypeRadio(viewModel.selectedTypeRadio.value!!, requireContext())
        } else {
            // todaydone and no need nodification
        }

        super.onPause()
    }

    private fun initTimer(){

        viewModel.timeStatus.value = PrefUtil.getTimerState(requireContext())
//        viewModel.selectedTypeRadio.value = PrefUtil.getSelectedTypeRadio(requireContext())

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


        if (viewModel.dailyTaskRemained.value!! <= 1) {
            when (viewModel.selectedTypeRadio.value) {
                R.id.radio_count_target -> onTimerFinished()
            }
        }
        else if (viewModel.timeStatus.value == TimerStatus.Running) {
            when (viewModel.selectedTypeRadio.value) {
                R.id.radio_count_target -> startTimer()
                R.id.radio_count_noLimit -> timeNumberGo()
            }
        }
        updateButtons()
//        updateCountdownUI()
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
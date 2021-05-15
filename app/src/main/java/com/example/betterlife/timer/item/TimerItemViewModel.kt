package com.example.betterlife.timer.item

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.betterlife.data.Plan
import com.example.betterlife.data.source.PlanRepository
import com.example.betterlife.databinding.FragmentTimerBinding
import com.example.betterlife.databinding.FragmentTimerItemBinding
import com.example.betterlife.util.PrefUtil
import java.util.*

class TimerItemViewModel(private val repository: PlanRepository): ViewModel() {


}
package com.scolley.betterlife.timer.item

import com.scolley.betterlife.R

enum class TimerStatus{
    Stopped, Running, Paused
}

enum class SelectedTypeRadio (val value: Int){
    DailyTarget(R.id.radio_count_target),
    NoLimit(R.id.radio_count_noLimit)
}
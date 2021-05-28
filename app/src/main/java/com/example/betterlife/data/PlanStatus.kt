package com.example.betterlife.data

import com.example.betterlife.R
import com.example.betterlife.util.Util.getString

enum class PlanStatus(val value: String) {

    Ongoing (getString(R.string.onGoing)),
    Success(getString(R.string.completed))
}
package com.example.betterlife.data

import com.example.betterlife.R
import com.example.betterlife.util.Util.getString

enum class PlanStatus(val value: String) {

    SingleWork (getString(R.string.SingleWork)),
    Teamwork (getString(R.string.Teamwork)),
    Success(getString(R.string.completed))
}
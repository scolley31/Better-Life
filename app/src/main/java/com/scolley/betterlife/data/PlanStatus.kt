package com.scolley.betterlife.data

import com.scolley.betterlife.R
import com.scolley.betterlife.util.Util.getString

enum class PlanStatus(val value: String) {

    SingleWork (getString(R.string.SingleWork)),
    Teamwork (getString(R.string.Teamwork)),
    Success(getString(R.string.completed))
}
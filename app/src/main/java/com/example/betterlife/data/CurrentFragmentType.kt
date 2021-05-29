package com.example.betterlife.data

import com.example.betterlife.R
import com.example.betterlife.util.Util.getString

enum class CurrentFragmentType(val value: String) {

    HOME(getString(R.string.personnal_goal)),
    OTHER(getString(R.string.other_goal)),
    TIMER(" "),
    LOGIN("")

}

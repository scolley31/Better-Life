package com.scolley.betterlife.data

import com.scolley.betterlife.R
import com.scolley.betterlife.util.Util.getString

enum class CurrentFragmentType(val value: String) {

    HOME(getString(R.string.personnal_goal)),
    OTHER(getString(R.string.other_goal)),
    TIMER(" "),
    LOGIN("")

}

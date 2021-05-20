package com.example.betterlife.timer

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.betterlife.R
import com.example.betterlife.data.Plan
import com.example.betterlife.timer.item.TimerInfoFragment
import com.example.betterlife.timer.item.TimerItemFragment


class TimerAdapter(fragmentManager: FragmentManager, val plan: Plan) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> TimerItemFragment(plan)
            1 -> TimerInfoFragment(plan)
            else -> TimerItemFragment(plan)
        }
    }

    override fun getCount() = TimerPage.values().size

    override fun getPageTitle(position: Int): CharSequence? {
        return null
    }
}

enum class TimerPage(val value: String) {
    Timer ("計時"),
    Analysis("分析"),
}
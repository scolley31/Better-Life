package com.example.betterlife.timer

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.betterlife.timer.item.TimerItemFragment

class TimeAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return TimerItemFragment(TimerPage.values()[position])
    }

    override fun getCount() = TimerPage.values().size

    override fun getPageTitle(position: Int): CharSequence? {
        return TimerPage.values()[position].value
    }
}

enum class TimerPage(val value: String) {
    Timer ("計時"),
    Analysis("分析"),

}
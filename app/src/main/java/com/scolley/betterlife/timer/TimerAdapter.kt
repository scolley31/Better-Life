package com.scolley.betterlife.timer

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.scolley.betterlife.R
import com.scolley.betterlife.data.Plan
import com.scolley.betterlife.data.PlanForShow
import com.scolley.betterlife.timer.item.*


class TimerAdapter(fragmentManager: FragmentManager, val plan: Plan) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when (position) {

            0 -> TimerItemFragment(plan)
            1 -> if (plan.dueDate == -1L) {
                TimerInfoFragment(plan)
            } else {
                TimerInfoDateFragment(plan)
            }
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
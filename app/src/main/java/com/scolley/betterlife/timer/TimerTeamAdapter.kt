package com.scolley.betterlife.timer

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.scolley.betterlife.data.PlanForShow
import com.scolley.betterlife.timer.team.TimerTeamDateFragment
import com.scolley.betterlife.timer.team.TimerTeamFragment
import com.scolley.betterlife.timer.team.TimerTeamItemFragment

class TimerTeamAdapter(fragmentManager: FragmentManager, val planTeam: PlanForShow?) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 ->  TimerTeamItemFragment(planTeam)
            1 ->  if (planTeam!!.dueDate == -1L) {
                TimerTeamFragment(planTeam)
            } else {
                TimerTeamDateFragment(planTeam)
            }
            else -> TimerTeamItemFragment(planTeam)

        }
    }

    override fun getCount() = TimerPage.values().size

    override fun getPageTitle(position: Int): CharSequence? {
        return null
    }
}

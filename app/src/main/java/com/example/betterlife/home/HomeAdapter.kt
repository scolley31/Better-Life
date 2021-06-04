package com.example.betterlife.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.betterlife.data.Plan
import com.example.betterlife.data.PlanStatus
import com.example.betterlife.data.User
import com.example.betterlife.home.item.HomeDoneFragment
import com.example.betterlife.home.item.HomeItemFragment
import com.example.betterlife.home.item.HomeTeamFragment
import com.example.betterlife.home.item.HomeTeamViewModel

class HomeAdapter(fragmentManager: FragmentManager,val user: User) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
           0 -> HomeItemFragment(user)
           1 -> HomeTeamFragment(user)
           2 -> HomeDoneFragment(user)
           else ->  HomeItemFragment(user)
        }
    }


    override fun getCount() = PlanStatus.values().size

    override fun getPageTitle(position: Int): CharSequence? {
        return PlanStatus.values()[position].value
    }
}


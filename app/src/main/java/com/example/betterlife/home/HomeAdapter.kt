package com.example.betterlife.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.betterlife.home.item.HomeItemFragment

class HomeAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return HomeItemFragment(PlanStatus.values()[position])
    }

    override fun getCount() = PlanStatus.values().size

    override fun getPageTitle(position: Int): CharSequence? {
        return PlanStatus.values()[position].value
    }
}

enum class PlanStatus(val value: String) {
    Ongoing ("進行中"),
    Success("已完成"),
    Fail("未完成")
}
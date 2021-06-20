package com.scolley.betterlife.timer.item

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.scolley.betterlife.data.Plan
import com.scolley.betterlife.data.Rank
import com.scolley.betterlife.data.source.FakeTestRepository
import com.scolley.betterlife.data.source.PlanRepository
import junit.framework.Assert.assertEquals
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


class TimerInfoViewModelTest {

    private lateinit var testRepository: FakeTestRepository

    private lateinit var viewModel: TimerInfoViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        testRepository = FakeTestRepository()
        viewModel = TimerInfoViewModel(testRepository)
    }

    @Test
    fun setRank_sortForRank() {

        val rank = mutableListOf<Rank>(
                Rank("1", "1", 456, 0, ""),
                Rank("2", "2", 654, 0, ""),
                Rank("3", "3", 0, 0, ""),
                Rank("4", "4", 1000, 0, "")
        )

        val result = viewModel.sortToRank(rank)

        assertEquals(Rank("4", "4", 1000, 0, ""), result[0])
        assertEquals(Rank("2", "2", 654, 0, ""), result[1])
        assertEquals(Rank("1", "1", 456, 0, ""), result[2])
        assertEquals(Rank("3", "3", 0, 0, ""), result[3])

    }
}
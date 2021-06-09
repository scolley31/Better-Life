package com.scolley.betterlife.util

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.scolley.betterlife.data.source.DefaultPlanRepository
import com.scolley.betterlife.data.source.PlanDataSource
import com.scolley.betterlife.data.source.PlanRepository
import com.scolley.betterlife.data.source.remote.PlanRemoteDataSource
import com.scolley.betterlife.data.local.PlanLocalDataSource

object ServiceLocator {

    @Volatile
    var repository: PlanRepository? = null
        @VisibleForTesting set

    fun provideRepository(context: Context): PlanRepository {
        synchronized(this) {
            return repository
                    ?: repository
                    ?: createPlanRepository(context)
        }
    }

    private fun createPlanRepository(context: Context): PlanRepository {
        return DefaultPlanRepository(
                PlanRemoteDataSource,
                createLocalDataSource(context)
        )
    }

    private fun createLocalDataSource(context: Context): PlanDataSource {
        return PlanLocalDataSource(context)
    }
}
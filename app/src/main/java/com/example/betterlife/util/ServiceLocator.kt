package com.example.betterlife.util

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.example.betterlife.data.Source.DefaultPlanRepository
import com.example.betterlife.data.Source.PlanDataSource
import com.example.betterlife.data.Source.PlanRepository
import com.example.betterlife.data.Source.remote.PlanRemoteDataSource
import com.example.betterlife.data.local.PlanLocalDataSource

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
package com.example.betterlife.data.source

import androidx.lifecycle.MutableLiveData
import com.example.betterlife.data.Plan
import com.example.betterlife.data.Result

class DefaultPlanRepository (private val remoteDataSource: PlanDataSource,
                             private val localDataSource: PlanDataSource

) : PlanRepository {

    override suspend fun addTask(plan: Plan): Result<Boolean> {
        return remoteDataSource.addTask(plan)
    }

    override suspend fun getPlanResult(): Result<List<Plan>> {
        return remoteDataSource.getPlanResult()
    }

    override fun getLivePlanResult(): MutableLiveData<List<Plan>> {
        return remoteDataSource.getLivePlanResult()
    }


}
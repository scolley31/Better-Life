package com.example.betterlife.data.source

import androidx.lifecycle.MutableLiveData
import com.example.betterlife.data.Plan
import com.example.betterlife.data.Result

interface PlanRepository {

    suspend fun addTask(plan: Plan): Result<Boolean>

    suspend fun getPlanResult(): Result<List<Plan>>

    suspend fun getOtherPlanResult(): Result<List<Plan>>

    fun getLivePlanResult(): MutableLiveData<List<Plan>>

}
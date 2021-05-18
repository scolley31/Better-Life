package com.example.betterlife.data.source

import com.example.betterlife.data.Plan
import com.example.betterlife.data.Result

interface PlanDataSource {

    suspend fun addTask(plan: Plan): Result<Boolean>


}
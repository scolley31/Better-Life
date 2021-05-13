package com.example.betterlife.data.Source

import com.example.betterlife.data.Source.PlanDataSource
import com.example.betterlife.data.Source.PlanRepository
import com.example.betterlife.data.local.PlanLocalDataSource

class DefaultPlanRepository (private val remoteDataSource: PlanDataSource,
                             private val localDataSource: PlanDataSource

) : PlanRepository {


}
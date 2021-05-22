package com.example.betterlife.data.source

import androidx.lifecycle.MutableLiveData
import com.example.betterlife.data.Completed
import com.example.betterlife.data.Plan
import com.example.betterlife.data.Result

class DefaultPlanRepository (private val remoteDataSource: PlanDataSource,
                             private val localDataSource: PlanDataSource

) : PlanRepository {

    override suspend fun deleteUserOngoingTask(userId: String, taskId: String): Result<Boolean>  {
        return remoteDataSource.deleteUserOngoingTask(userId, taskId)
    }

    override suspend fun deleteTask(taskId: String): Result<Boolean> {
        return remoteDataSource.deleteTask(taskId)
    }

    override suspend fun addTask(plan: Plan): Result<Boolean> {
        return remoteDataSource.addTask(plan)
    }

    override suspend fun sendCompleted(completed: Completed, taskID: String): Result<Boolean> {
        return remoteDataSource.sendCompleted(completed, taskID)
    }

    override suspend fun getCompleted(taskID: String, userID:String): Result<List<Completed>> {
        return remoteDataSource.getCompleted(taskID, userID)
    }

    override suspend fun getPlanResult(): Result<List<Plan>> {
        return remoteDataSource.getPlanResult()
    }

    override suspend fun getOtherPlanResult(): Result<List<Plan>> {
        return remoteDataSource.getOtherPlanResult()
    }

    override fun getLivePlanResult(): MutableLiveData<List<Plan>> {
        return remoteDataSource.getLivePlanResult()
    }


}
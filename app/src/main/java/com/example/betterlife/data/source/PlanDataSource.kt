package com.example.betterlife.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.betterlife.data.Completed
import com.example.betterlife.data.Plan
import com.example.betterlife.data.Result
import com.example.betterlife.data.User

interface PlanDataSource {

    suspend fun findUser(firebaseUserId: String): Result<User?>

    suspend fun createUser(user: User): Result<Boolean>

    suspend fun taskFinish(taskId: String): Result<Boolean>

    suspend fun addToOtherTask(userId: String, taskId: String): Result<Boolean>

    suspend fun deleteUserOngoingTask(userId: String, taskId: String): Result<Boolean>

    suspend fun deleteTask(taskId: String): Result<Boolean>

    suspend fun addTask(plan: Plan): Result<Boolean>

    suspend fun sendCompleted(completed: Completed, taskID: String): Result<Boolean>

    suspend fun getCompleted(taskID: String, userID:String): Result<List<Completed>>

    suspend fun getPlanResult(): Result<List<Plan>>

    suspend fun getFinishedPlanResult(): Result<List<Plan>>

    suspend fun getOtherPlanResult(): Result<List<Plan>>

    suspend fun getOtherSelectedPlanResult(categoryID: String): Result<List<Plan>>

    fun getUser(userId: String): LiveData<User>

    fun getLivePlanResult(): MutableLiveData<List<Plan>>

    fun getLiveOtherPlanResult(): MutableLiveData<List<Plan>>

    fun getLiveOtherSelectedPlanResult(categoryID: String): MutableLiveData<List<Plan>>


}
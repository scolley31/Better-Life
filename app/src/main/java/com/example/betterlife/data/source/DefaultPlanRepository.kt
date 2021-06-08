package com.example.betterlife.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.betterlife.data.*

class DefaultPlanRepository (private val remoteDataSource: PlanDataSource,
                             private val localDataSource: PlanDataSource

) : PlanRepository {

    override suspend fun findAllUser(): Result<List<User?>>   {
        return remoteDataSource.findAllUser()
    }

    override suspend fun findUser(firebaseUserId: String): Result<User?>  {
        return remoteDataSource.findUser(firebaseUserId)
    }

    override suspend fun findUserByName(userName: String): Result<Boolean>  {
        return remoteDataSource.findUserByName(userName)
    }

    override suspend fun createUser(user: User): Result<Boolean>  {
        return remoteDataSource.createUser(user)
    }

    override suspend fun taskFinish(taskId: String): Result<Boolean>  {
        return remoteDataSource.taskFinish(taskId)
    }

    override suspend fun addToOtherTask(userId: String, taskId: String): Result<Boolean>  {
        return remoteDataSource.addToOtherTask(userId, taskId)
    }

    override suspend fun deleteUserOngoingTask(userId: String, taskId: String): Result<Boolean>  {
        return remoteDataSource.deleteUserOngoingTask(userId, taskId)
    }

    override suspend fun deleteTask(taskId: String): Result<Boolean> {
        return remoteDataSource.deleteTask(taskId)
    }

    override suspend fun addTask(plan: Plan): Result<String> {
        return remoteDataSource.addTask(plan)
    }

    override suspend fun addGroup(group: Groups, taskID: String): Result<Boolean> {
        return remoteDataSource.addGroup(group,taskID)
    }

    override suspend fun sendCompleted(completed: Completed, taskID: String): Result<Boolean> {
        return remoteDataSource.sendCompleted(completed, taskID)
    }

    override suspend fun getCompleted(taskID: String, userID:String): Result<List<Completed>> {
        return remoteDataSource.getCompleted(taskID, userID)
    }

    override suspend fun getGroup(taskID: String, userID:String): Result<List<Groups>> {
        return remoteDataSource.getGroup(taskID, userID)
    }

    override suspend fun getFinishedPlanResult(): Result<List<Plan>> {
        return remoteDataSource.getFinishedPlanResult()
    }

    override suspend fun getPlanResult(): Result<List<Plan>> {
        return remoteDataSource.getPlanResult()
    }

    override suspend fun getGroupPlanResult(): Result<List<Plan>> {
        return remoteDataSource.getGroupPlanResult()
    }

    override suspend fun getOtherPlanResult(): Result<List<Plan>> {
        return remoteDataSource.getOtherPlanResult()
    }

    override suspend fun getOtherSelectedPlanResult(categoryID: String): Result<List<Plan>> {
        return remoteDataSource.getOtherSelectedPlanResult(categoryID)
    }

    override fun getLivePlanResult(): MutableLiveData<List<Plan>> {
        return remoteDataSource.getLivePlanResult()
    }

    override fun getLiveOtherPlanResult(): MutableLiveData<List<Plan>> {
        return remoteDataSource.getLiveOtherPlanResult()
    }

    override fun getLiveOtherSelectedPlanResult(categoryID: String): MutableLiveData<List<Plan>> {
        return remoteDataSource.getLiveOtherSelectedPlanResult(categoryID)
    }

    override fun getUser(userId: String): LiveData<User> {
        return remoteDataSource.getUser(userId)
    }

}
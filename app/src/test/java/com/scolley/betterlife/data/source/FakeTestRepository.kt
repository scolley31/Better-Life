package com.scolley.betterlife.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.scolley.betterlife.data.*
import kotlinx.coroutines.runBlocking

class FakeTestRepository: PlanRepository {

    var planServiceData: LinkedHashMap<String, Plan> = LinkedHashMap()

    private val observablePlan = MutableLiveData<Result<List<Plan>>>()

//    override suspend fun getPlanResult(forceUpdate: Boolean): Result<List<Plan>> {
//        return Result.Success(planServiceData.values.toList())
//    }
//
//    override suspend fun refreshTasks() {
//        observablePlan.value = getPlanResult()
//    }
//
//    override fun observePlans(): LiveData<Result<List<Plan>>> {
//        runBlocking { refreshTasks() }
//        return observablePlan
//    }
//
//    fun addPlans(vararg plans: Plan) {
//        for (plan in plans) {
//            planServiceData[plan.id] = plan
//        }
//        runBlocking { refreshTasks() }
//    }



    override suspend fun findUser(firebaseUserId: String): Result<User?> {
        TODO("Not yet implemented")
    }

    override suspend fun findAllUser(): Result<List<User?>> {
        TODO("Not yet implemented")
    }

    override suspend fun createUser(user: User): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun findUserByName(userName: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun taskFinish(taskId: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun addToOtherTask(userId: String, taskId: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUserOngoingTask(userId: String, taskId: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(taskId: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun addTask(plan: Plan): Result<String> {
        TODO("Not yet implemented")
    }

    override suspend fun addGroup(group: Groups, taskID: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun sendCompleted(completed: Completed, taskID: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getCompleted(taskID: String, userID: String): Result<List<Completed>> {
        TODO("Not yet implemented")
    }

    override suspend fun getGroup(taskID: String, userID: String): Result<List<Groups>> {
        TODO("Not yet implemented")
    }

    override suspend fun getPlanResult(): Result<List<Plan>> {
        TODO("Not yet implemented")
    }

    override suspend fun getGroupPlanResult(): Result<List<Plan>> {
        TODO("Not yet implemented")
    }

    override suspend fun getFinishedPlanResult(): Result<List<Plan>> {
        TODO("Not yet implemented")
    }

    override suspend fun getOtherPlanResult(): Result<List<Plan>> {
        TODO("Not yet implemented")
    }

    override suspend fun getOtherSelectedPlanResult(categoryID: String): Result<List<Plan>> {
        TODO("Not yet implemented")
    }

    override fun getUser(userId: String): LiveData<User> {
        TODO("Not yet implemented")
    }

    override fun getLivePlanResult(): MutableLiveData<List<Plan>> {
        TODO("Not yet implemented")
    }

    override fun getLiveOtherPlanResult(): MutableLiveData<List<Plan>> {
        TODO("Not yet implemented")
    }

    override fun getLiveOtherSelectedPlanResult(categoryID: String): MutableLiveData<List<Plan>> {
        TODO("Not yet implemented")
    }
}
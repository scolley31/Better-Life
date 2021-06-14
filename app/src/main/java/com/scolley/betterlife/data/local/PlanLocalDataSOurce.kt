package com.scolley.betterlife.data.local

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.scolley.betterlife.data.*
import com.scolley.betterlife.data.source.PlanDataSource

class PlanLocalDataSource(val context: Context) : PlanDataSource {

//    override suspend fun login(id: String): Result<User> {
//        return when (id) {
//            "waynechen323" -> Result.Success((Author(
//                    id,
//                    "AKA小安老師",
//                    "wayne@school.appworks.tw"
//            )))
//            "dlwlrma" -> Result.Success((Author(
//                    id,
//                    "IU",
//                    "dlwlrma@school.appworks.tw"
//            )))
//            //TODO add your profile here
//            else -> Result.Fail("You have to add $id info in local data source")
//        }
//    }

    override suspend fun findAllUser(): Result<List<User?>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun findUser(firebaseUserId: String): Result<User?> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun findUserByName(userName: String): Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun createUser(user: User): Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun taskFinish(userId: String): Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun addToOtherTask(userId: String, taskId: String): Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteUserOngoingTask(userId: String, taskId: String): Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteTask(taskId: String): Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getPlanResult(): com.scolley.betterlife.data.Result<List<Plan>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getGroupPlanResult(): Result<List<Plan>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getFinishedPlanResult(): com.scolley.betterlife.data.Result<List<Plan>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getOtherSelectedPlanResult(categoryID: String): com.scolley.betterlife.data.Result<List<Plan>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getOtherPlanResult(): com.scolley.betterlife.data.Result<List<Plan>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLiveOtherSelectedPlanResult(categoryID: String): MutableLiveData<List<Plan>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLiveOtherPlanResult(): MutableLiveData<List<Plan>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLivePlanResult(): MutableLiveData<List<Plan>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUser(userId: String): LiveData<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun addTask(plan: Plan): com.scolley.betterlife.data.Result<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun addGroup(group: Groups, taskID: String): Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun sendCompleted(completed: Completed, taskID: String): com.scolley.betterlife.data.Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getCompleted(taskID: String, userID:String): Result<List<Completed>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getGroup(taskID: String, userID:String): Result<List<Groups>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    suspend fun delete(plan: Plan): com.scolley.betterlife.data.Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

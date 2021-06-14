package com.scolley.betterlife.data.source.remote

import android.content.ContentValues.TAG
import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.scolley.betterlife.PlanApplication
import com.scolley.betterlife.PlanApplication.Companion.instance
import com.scolley.betterlife.R
import com.scolley.betterlife.data.*
import com.scolley.betterlife.data.source.PlanDataSource
import com.scolley.betterlife.util.Logger
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query

object PlanRemoteDataSource : PlanDataSource {

    private const val PATH_USERS = "users"
    private const val PATH_PLANS = "plan"
    private const val KEY_CREATED_TIME = "createdTime"
    private const val KEY_COMPLETED_TIME = "date"
    private const val KEY_PLAN_MEMBER = "members"
    private const val KEY_PLAN_CATEGORY = "category"
    private const val KEY_PLAN_COMPLETEDLIST = "completedList"
    private const val KEY_PLAN_GROUPS = "groups"
    private const val KEY_PLAN_TASKDONE = "taskDone"
    private const val KEY_PLAN_USERID = "user_id"
    private const val KEY_PLAN_GROUP = "group"
    private const val KEY_USER_ID = "userID"
    private const val KEY_USER_NAME = "userName"
    private const val KEY_PLAN_GROUP_MEMBER = "membersID"



    override fun getUser(userId: String): LiveData<User> {
        val user = MutableLiveData<User>()
        FirebaseFirestore.getInstance().collection(PATH_USERS).document(userId)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Logger.w("[${this::class.simpleName}] Error getting documents. ${e.message}")
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        user.value = snapshot.toObject(User::class.java)!!

                    } else {
                        Logger.d("Current data: null")
                    }
                }

        return user
    }

    override suspend fun findAllUser(): Result<List<User?>> =
            suspendCoroutine { continuation ->
                FirebaseFirestore.getInstance()
                        .collection(PATH_USERS)
                        .get()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val list = mutableListOf<User>()
                                for (document in task.result!!) {
                                    Logger.d(document.id + " => " + document.data)

                                    val completed = document.toObject(User::class.java)
                                    list.add(completed)
                                }
                                continuation.resume(Result.Success(list))
                            } else {
                                task.exception?.let {

                                    Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                                    continuation.resume(Result.Error(it))
                                    return@addOnCompleteListener
                                }
                                continuation.resume(Result.Fail(PlanApplication.instance.getString(R.string.you_know_nothing)))
                            }
                        }
            }

    override suspend fun findUser(firebaseUserId: String): Result<User?> =
            suspendCoroutine { continuation ->
                FirebaseFirestore.getInstance().collection(PATH_USERS).document(firebaseUserId)
                        .get()
                        .addOnCompleteListener { findUser ->
                            if (findUser.isSuccessful) {
                                findUser.result?.let { documentU ->
                                    val user = documentU.toObject(User::class.java)
                                    continuation.resume(Result.Success(user))
                                }
                            } else {
                                findUser.exception?.let {
                                    Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                                    continuation.resume(Result.Error(it))
                                    return@addOnCompleteListener
                                }
                                continuation.resume(Result.Fail(instance.getString(R.string.you_know_nothing)))
                            }
                        }
            }


    override suspend fun findUserByName(userName: String): Result<Boolean> =
            suspendCoroutine { continuation ->
                FirebaseFirestore.getInstance().collection(PATH_USERS)
                        .whereEqualTo(KEY_USER_NAME,userName)
                        .get()
                        .addOnCompleteListener { findUser ->
                            if (findUser.isSuccessful) {
                                findUser.result?.let { documentU ->
                                    if (findUser.result!!.isEmpty) {
                                        continuation.resume(Result.Success(true))
                                    } else {
                                        continuation.resume(Result.Success(false))
                                    }
                                }
                            } else {
                                findUser.exception?.let {
                                    Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                                    continuation.resume(Result.Error(it))
                                    return@addOnCompleteListener
                                }
                                continuation.resume(Result.Fail(instance.getString(R.string.you_know_nothing)))
                            }
                        }
            }


    override suspend fun createUser(user: User): Result<Boolean> =
            suspendCoroutine { continuation ->
                FirebaseFirestore.getInstance().collection(PATH_USERS).document(user.userId).set(user)
                        .addOnCompleteListener { addUser ->
                            if (addUser.isSuccessful) {
                                continuation.resume(Result.Success(true))
                            } else {
                                addUser.exception?.let {

                                    Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                                    continuation.resume(Result.Error(it))
                                }
                                continuation.resume(Result.Fail(instance.getString(R.string.you_know_nothing)))
                            }
                        }
            }


    override suspend fun taskFinish(taskId: String): Result<Boolean> =
        suspendCoroutine {
            FirebaseFirestore.getInstance().collection(PATH_PLANS).document(taskId)
                    .update(KEY_PLAN_TASKDONE,true)
                    .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
                    .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
        }


    override suspend fun deleteTask(taskId: String): Result<Boolean> =
        suspendCoroutine { continuation ->
            FirebaseFirestore.getInstance().collection(PATH_PLANS).document(taskId)
                .delete()
                .addOnCompleteListener { addId ->
                    if (addId.isSuccessful) {
                        continuation.resume(Result.Success(true))
                    } else {
                        addId.exception?.let {

                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                        }
                        continuation.resume(Result.Fail(PlanApplication.instance.getString(R.string.you_know_nothing)))
                    }
                }
        }

    override suspend fun deleteUserOngoingTask(userId: String, taskId: String): Result<Boolean> =
        suspendCoroutine { continuation ->
            FirebaseFirestore.getInstance().collection(PATH_PLANS).document(taskId)
                .update(KEY_PLAN_MEMBER, FieldValue.arrayRemove(userId))
                .addOnCompleteListener { addId ->
                    if (addId.isSuccessful) {
                        continuation.resume(Result.Success(true))
                    } else {
                        addId.exception?.let {

                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                        }
                        continuation.resume(Result.Fail(PlanApplication.instance.getString(R.string.you_know_nothing)))
                    }
                }
        }


    override suspend fun addToOtherTask(userId: String, taskId: String): Result<Boolean> =
        suspendCoroutine { continuation ->
            FirebaseFirestore.getInstance().collection(PATH_PLANS).document(taskId)
                .update(KEY_PLAN_MEMBER, FieldValue.arrayUnion(userId))
                .addOnCompleteListener { addId ->
                    if (addId.isSuccessful) {
                        continuation.resume(Result.Success(true))
                    } else {
                        addId.exception?.let {

                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                        }
                        continuation.resume(Result.Fail(PlanApplication.instance.getString(R.string.you_know_nothing)))
                    }
                }
        }


    override suspend fun getCompleted(taskID: String, userID:String): Result<List<Completed>> =
        suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
                .collection(PATH_PLANS)
                .document(taskID)
                .collection(KEY_PLAN_COMPLETEDLIST)
                .whereEqualTo(KEY_PLAN_USERID,userID)
                .orderBy(KEY_COMPLETED_TIME, Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val list = mutableListOf<Completed>()
                        for (document in task.result!!) {
                            Logger.d(document.id + " => " + document.data)

                            val completed = document.toObject(Completed::class.java)
                            list.add(completed)
                        }
                        continuation.resume(Result.Success(list))
                    } else {
                        task.exception?.let {

                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(PlanApplication.instance.getString(R.string.you_know_nothing)))
                    }
                }
    }


    override suspend fun getGroup(taskID: String, userID:String): Result<List<Groups>> =
            suspendCoroutine { continuation ->
                FirebaseFirestore.getInstance()
                        .collection(PATH_PLANS)
                        .document(taskID)
                        .collection(KEY_PLAN_GROUPS)
                        .whereArrayContains(KEY_PLAN_GROUP_MEMBER,userID)
                        .get()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val list = mutableListOf<Groups>()
                                for (document in task.result!!) {
                                    Logger.d(document.id + " => " + document.data)

                                    val groups = document.toObject(Groups::class.java)
                                    list.add(groups)
                                }
                                continuation.resume(Result.Success(list))
                            } else {
                                task.exception?.let {

                                    Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                                    continuation.resume(Result.Error(it))
                                    return@addOnCompleteListener
                                }
                                continuation.resume(Result.Fail(PlanApplication.instance.getString(R.string.you_know_nothing)))
                            }
                        }
            }


    override suspend fun getFinishedPlanResult(): Result<List<Plan>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
                .collection(PATH_PLANS)
                .whereEqualTo(KEY_PLAN_TASKDONE,true)
                .whereArrayContains(KEY_PLAN_MEMBER,"Scolley")
                .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val list = mutableListOf<Plan>()
                        for (document in task.result!!) {
                            Logger.d(document.id + " => " + document.data)

                            val article = document.toObject(Plan::class.java)
                            list.add(article)
                        }
                        continuation.resume(Result.Success(list))
                    } else {
                        task.exception?.let {

                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(PlanApplication.instance.getString(R.string.you_know_nothing)))
                    }
                }
    }


    override suspend fun getPlanResult(): Result<List<Plan>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection(PATH_PLANS)
            .whereArrayContains(KEY_PLAN_MEMBER,FirebaseAuth.getInstance().currentUser!!.uid)
            .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<Plan>()
                    for (document in task.result!!) {
                        Logger.d(document.id + " => " + document.data)

                        val article = document.toObject(Plan::class.java)
                        list.add(article)
                    }
                    continuation.resume(Result.Success(list))
                } else {
                    task.exception?.let {

                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail(PlanApplication.instance.getString(R.string.you_know_nothing)))
                }
            }
    }

    override suspend fun getGroupPlanResult(): Result<List<Plan>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
                .collection(PATH_PLANS)
                .whereArrayContains(KEY_PLAN_MEMBER,FirebaseAuth.getInstance().currentUser!!.uid)
                .whereEqualTo(KEY_PLAN_GROUP, true)
                .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val list = mutableListOf<Plan>()
                        for (document in task.result!!) {
                            Logger.d(document.id + " getGroupPlanResult=> " + document.data)

                            val article = document.toObject(Plan::class.java)
                            list.add(article)
                        }
                        continuation.resume(Result.Success(list))
                    } else {
                        task.exception?.let {

                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(PlanApplication.instance.getString(R.string.you_know_nothing)))
                    }
                }
    }

    override suspend fun getOtherSelectedPlanResult(categoryID: String): Result<List<Plan>> =
        suspendCoroutine { continuation ->
            FirebaseFirestore.getInstance()
                .collection(PATH_PLANS)
                .whereEqualTo(KEY_PLAN_CATEGORY,categoryID)
                .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val list = mutableListOf<Plan>()
                        for (document in task.result!!) {
                            Logger.d(document.id + " => " + document.data)

                            val article = document.toObject(Plan::class.java)
                            list.add(article)
                        }
                        continuation.resume(Result.Success(list))
                    } else {
                        task.exception?.let {

                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(PlanApplication.instance.getString(R.string.you_know_nothing)))
                    }
                }
        }

    override suspend fun getOtherPlanResult(): Result<List<Plan>> =
        suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection(PATH_PLANS)
            .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<Plan>()
                    for (document in task.result!!) {
                        Logger.d(document.id + " => " + document.data)

                        val article = document.toObject(Plan::class.java)
                        list.add(article)
                    }
                    continuation.resume(Result.Success(list))
                } else {
                    task.exception?.let {

                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail(PlanApplication.instance.getString(R.string.you_know_nothing)))
                }
            }
    }

    override fun getLiveOtherPlanResult(): MutableLiveData<List<Plan>> {

        val liveData = MutableLiveData<List<Plan>>()

        FirebaseFirestore.getInstance()
            .collection(PATH_PLANS)
            .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->

                Logger.i("addSnapshotListener detect")

                exception?.let {
                    Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                val list = mutableListOf<Plan>()
                for (document in snapshot!!) {
                    Logger.d(document.id + " => " + document.data)

                    val article = document.toObject(Plan::class.java)
                    list.add(article)
                }

                liveData.value = list
            }
        return liveData
    }

    override fun getLiveOtherSelectedPlanResult(categoryID: String): MutableLiveData<List<Plan>> {

        val liveData = MutableLiveData<List<Plan>>()

        FirebaseFirestore.getInstance()
            .collection(PATH_PLANS)
            .whereEqualTo(KEY_PLAN_CATEGORY,categoryID)
            .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->

                Logger.i("addSnapshotListener detect")

                exception?.let {
                    Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                val list = mutableListOf<Plan>()
                for (document in snapshot!!) {
                    Logger.d(document.id + " => " + document.data)

                    val article = document.toObject(Plan::class.java)
                    list.add(article)
                }

                liveData.value = list
            }
        return liveData
    }

    override fun getLivePlanResult(): MutableLiveData<List<Plan>> {

        val liveData = MutableLiveData<List<Plan>>()

        FirebaseFirestore.getInstance()
            .collection(PATH_PLANS)
            .whereArrayContains(KEY_PLAN_MEMBER,FirebaseAuth.getInstance().currentUser!!.uid)
            .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->

                Logger.i("addSnapshotListener detect")

                exception?.let {
                    Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                val list = mutableListOf<Plan>()
                for (document in snapshot!!) {
                    Logger.d(document.id + " => " + document.data)

                    val article = document.toObject(Plan::class.java)
                    list.add(article)
                }

                liveData.value = list
            }
        return liveData
    }


    override suspend fun addTask(plan: Plan): Result<String> =
        suspendCoroutine { continuation ->
        val plans = FirebaseFirestore.getInstance().collection(PATH_PLANS)
        val document = plans.document()

        plan.id = document.id
        plan.createdTime = Calendar.getInstance().timeInMillis

        document
            .set(plan)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Logger.i("Publish: $plan")

                    continuation.resume(Result.Success(plan.id))
                } else {
                    task.exception?.let {

                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail(PlanApplication.instance.getString(R.string.you_know_nothing)))
                }
            }
    }

    override suspend fun sendCompleted(completed: Completed, taskID: String): Result<Boolean> =
        suspendCoroutine { continuation ->
        val plans = FirebaseFirestore.getInstance().collection(PATH_PLANS).document(taskID)
        val subCollection = plans.collection(KEY_PLAN_COMPLETEDLIST)
        val document = subCollection.document()

        completed.id = document.id

        document.set(completed)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Logger.i("Publish: $completed")

                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {

                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(PlanApplication.instance.getString(R.string.you_know_nothing)))
                    }
                }
    }

    override suspend fun addGroup(group: Groups, taskID: String): Result<Boolean> =
            suspendCoroutine { continuation ->
                val plans = FirebaseFirestore.getInstance().collection(PATH_PLANS).document(taskID)
                val subCollection = plans.collection(KEY_PLAN_GROUPS)
                val document = subCollection.document()

                group.id = document.id

                document.set(group)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Logger.i("Publish: $group")

                                continuation.resume(Result.Success(true))
                            } else {
                                task.exception?.let {

                                    Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                                    continuation.resume(Result.Error(it))
                                    return@addOnCompleteListener
                                }
                                continuation.resume(Result.Fail(PlanApplication.instance.getString(R.string.you_know_nothing)))
                            }
                        }
            }

}
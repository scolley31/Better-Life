package com.example.betterlife.data.source.remote

import android.icu.util.Calendar
import androidx.lifecycle.MutableLiveData
import com.example.betterlife.PlanApplication
import com.example.betterlife.R
import com.example.betterlife.data.Plan
import com.example.betterlife.data.source.PlanDataSource
import com.example.betterlife.util.Logger
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import com.example.betterlife.data.Result
import com.google.firebase.firestore.Query

object PlanRemoteDataSource : PlanDataSource {

    private const val PATH_ARTICLES = "plan"
    private const val KEY_CREATED_TIME = "createdTime"

    override suspend fun getPlanResult(): Result<List<Plan>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection(PATH_ARTICLES)
            .whereArrayContains("members","Scolley")
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

    override fun getLivePlanResult(): MutableLiveData<List<Plan>> {

        val liveData = MutableLiveData<List<Plan>>()

        FirebaseFirestore.getInstance()
            .collection(PATH_ARTICLES)
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

    override suspend fun addTask(plan: Plan): Result<Boolean> = suspendCoroutine { continuation ->
        val plans = FirebaseFirestore.getInstance().collection(PATH_ARTICLES)
        val document = plans.document()

        plan.id = document.id
        plan.createdTime = Calendar.getInstance().timeInMillis

        document
            .set(plan)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Logger.i("Publish: $plan")

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
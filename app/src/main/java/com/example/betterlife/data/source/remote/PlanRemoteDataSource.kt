package com.example.betterlife.data.source.remote

import android.icu.util.Calendar
import com.example.betterlife.PlanApplication
import com.example.betterlife.R
import com.example.betterlife.data.Plan
import com.example.betterlife.data.source.PlanDataSource
import com.example.betterlife.util.Logger
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import com.example.betterlife.data.Result

object PlanRemoteDataSource : PlanDataSource {

    private const val PATH_ARTICLES = "plan"
    private const val KEY_CREATED_TIME = "createdTime"

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
package com.example.betterlife.data.local

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.betterlife.data.Plan
import com.example.betterlife.data.source.PlanDataSource

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

    suspend fun getArticles(): com.example.betterlife.data.Result<List<Plan>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getLiveArticles(): MutableLiveData<List<Plan>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    suspend fun publish(plan: Plan): com.example.betterlife.data.Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    suspend fun delete(plan: Plan): com.example.betterlife.data.Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

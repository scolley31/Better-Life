package com.example.betterlife

import android.app.Application
import com.example.betterlife.data.Source.PlanRepository
import com.example.betterlife.util.ServiceLocator
import kotlin.properties.Delegates

class PlanApplication : Application() {

    // Depends on the flavor,
    val repository: PlanRepository
        get() = ServiceLocator.provideRepository(this)

    companion object {
        var instance: PlanApplication by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun isLiveDataDesign() = true
}

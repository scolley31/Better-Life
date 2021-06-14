package com.scolley.betterlife

import android.app.Application
import android.content.Context
import com.scolley.betterlife.data.source.PlanRepository
import com.scolley.betterlife.util.ServiceLocator
import kotlin.properties.Delegates

class PlanApplication : Application() {

    // Depends on the flavor,
    val repository: PlanRepository
        get() = ServiceLocator.provideRepository(this)

    companion object {
        var instance: PlanApplication by Delegates.notNull()
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        appContext = applicationContext
    }

}

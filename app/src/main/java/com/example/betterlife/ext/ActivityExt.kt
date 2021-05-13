package com.example.betterlife.ext

import android.app.Activity
import android.view.Gravity
import android.widget.Toast
import com.example.betterlife.PlanApplication
import com.example.betterlife.factory.ViewModelFactory

fun Activity.getVmFactory(): ViewModelFactory {
    val repository = (applicationContext as PlanApplication).repository
    return ViewModelFactory(repository)
}

fun Activity?.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).apply {
        setGravity(Gravity.CENTER, 0, 0)
        show()
    }
}
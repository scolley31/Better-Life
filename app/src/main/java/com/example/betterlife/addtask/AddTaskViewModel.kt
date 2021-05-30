package com.example.betterlife.addtask

import android.graphics.Insets.add
import android.icu.util.Calendar
import androidx.databinding.InverseMethod
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.betterlife.PlanApplication
import com.example.betterlife.R
import com.example.betterlife.data.Plan
import com.example.betterlife.data.Result
import com.example.betterlife.data.User
import com.example.betterlife.data.source.PlanRepository
import com.example.betterlife.newwork.LoadApiStatus
import com.example.betterlife.util.Logger
import com.example.betterlife.util.Util.getString
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class AddTaskViewModel(private val repository: PlanRepository): ViewModel() {

    val name = MutableLiveData<String?>()

    val target = MutableLiveData<Long>()

    val dailyTarget = MutableLiveData<Int>()

    val category = MutableLiveData<String>()

    val selectedTypeRadio = MutableLiveData<Int>()

    var dueDate = MutableLiveData<Long>().apply {
        value = Calendar.getInstance().timeInMillis
    }

//    val seletedType: String
//        get() = when (selectedTypeRadio.value) {
//            R.id.radio_enddate -> getString(R.string.text_selectDate)
//            R.id.radio_totaltime -> getString(R.string.text_selectTime)
//            else -> ""
//        }

    private val _user = MutableLiveData<User>()

    val user: LiveData<User>
        get() = _user

    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _error = MutableLiveData<String?>()

    val error: LiveData<String?>
        get() = _error

    private val _navigateToHome = MutableLiveData<Boolean>()

    val navigateToHome: MutableLiveData<Boolean>
        get() = _navigateToHome

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        Logger.i("------------------------------------")
        Logger.i("[${this::class.simpleName}]${this}")
        Logger.i("------------------------------------")
        name.value = ""
        dailyTarget.value = 0
        target.value = 0
        dueDate.value = -1
    }

    fun navigateToHome () {
        _navigateToHome.value = true
    }

    fun navigateToHomeAfterSend (needRefresh: Boolean = false) {
        _navigateToHome.value = needRefresh
    }


    @InverseMethod("convertLongToString")
    fun convertStringToLong(value: String): Long {
        return try {
            value.toLong()
//                    .let {
//                when (it) {
//                    0L -> 1
//                    else -> it
//                }
//            }
        } catch (e: NumberFormatException) {
            1
        }
    }

    fun convertLongToString(value: Long): String {
        return value.toString()
    }

    fun addTask(){

        coroutineScope.launch {

            val newTask = Plan(
                    members = listOf(FirebaseAuth.getInstance().currentUser!!.uid),
                    name = name.value!!,
                    dailyTarget = dailyTarget.value!!,
                    category = category.value!!,
                    target = target.value!!.toInt(),
                    dueDate = dueDate.value!!
                )

            when (val result = repository.addTask(newTask)) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    navigateToHomeAfterSend(true)
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                }
                else -> {
                    _error.value = PlanApplication.instance.getString(R.string.you_know_nothing)
                    _status.value = LoadApiStatus.ERROR
                }
            }
        }
    }

}
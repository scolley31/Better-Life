package com.example.betterlife.addtask

import android.graphics.Insets.add
import android.icu.util.Calendar
import android.util.Log
import androidx.databinding.InverseMethod
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.betterlife.PlanApplication
import com.example.betterlife.R
import com.example.betterlife.data.Groups
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
import kotlinx.coroutines.test.withTestContext
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

    val isGroup = MutableLiveData<Boolean>()

    val partner = MutableLiveData<String>()

    private val _users = MutableLiveData<List<User?>>()

    val users: LiveData<List<User?>>
        get() = _users

    var userName = MutableLiveData<List<String>>()

    var userID = MutableLiveData<List<String>>()

    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _error = MutableLiveData<String?>()

    val error: LiveData<String?>
        get() = _error

    private val _refreshStatus = MutableLiveData<Boolean>()

    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus

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
        target.value = 1
        dueDate.value = -1
        isGroup.value = false

        findAllUser()

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
                    .let {
                when (it) {
                    0L -> 1
                    else -> it
                }
            }
        } catch (e: NumberFormatException) {
            1
        }
    }

    fun convertLongToString(value: Long): String {
        return value.toString()
    }

    fun findAllUser(){

        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            val result = repository.findAllUser()
            Log.d("test","findAllUser = $result")

            _users.value = when (result) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    result.data
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                else -> {
                    _error.value = PlanApplication.instance.getString(R.string.you_know_nothing)
                    _status.value = LoadApiStatus.ERROR
                    null
//                }
                }
            }
            userToArray()
            _refreshStatus.value = false
        }

    }

    fun userToArray(){
        var listName = mutableListOf<String>()
        var listID = mutableListOf<String>()
        for (i in users.value!!.indices){
            if (users.value!![i]!!.userId != FirebaseAuth.getInstance().currentUser!!.uid) {
                listName.run {
                    add(users.value!![i]!!.userName)
                }
                listID.run {
                    add(users.value!![i]!!.userId)
                }
            }
        }
        userName.value = listName
        Log.d("test","listName = $listName")
        userID.value = listID
        Log.d("test","listID = $listID")
    }


    fun addTask(){

        coroutineScope.launch {

            var partnerID: String = ""
            if (isGroup.value == true) {
                for (i in users.value!!.indices) {
                    if (users.value!![i]!!.userName == partner.value){
                        partnerID = users.value!![i]!!.userId
                        break
                    }
                }
            }
            Log.d("test","partnerID =$partnerID")

            val newTask = Plan(
                    members = if(isGroup.value == false){
                        listOf(FirebaseAuth.getInstance().currentUser!!.uid)
                    } else {
                        listOf(FirebaseAuth.getInstance().currentUser!!.uid, partnerID )
                    },
                    name = name.value!!,
                    dailyTarget = dailyTarget.value!!,
                    category = category.value!!,
                    target = target.value!!.toInt(),
                    dueDate = dueDate.value!!,
                    group = isGroup.value!!
                )
            var taskID : String

            taskID = when (val result = repository.addTask(newTask)) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    result.data
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                    null.toString()
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                    null.toString()
                }
                else -> {
                    _error.value = PlanApplication.instance.getString(R.string.you_know_nothing)
                    _status.value = LoadApiStatus.ERROR
                    null.toString()
                }
            }

            if (isGroup.value == true) {
                val newGroup = Groups(
                    membersID = listOf(FirebaseAuth.getInstance().currentUser!!.uid, partnerID)
                )
            when (val result = repository.addGroup(newGroup,taskID)) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
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
            navigateToHomeAfterSend(true)
        }
    }

}
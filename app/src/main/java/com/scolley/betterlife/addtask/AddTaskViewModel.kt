package com.scolley.betterlife.addtask

import android.icu.util.Calendar
import android.util.Log
import android.widget.Toast
import androidx.databinding.InverseMethod
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.scolley.betterlife.PlanApplication
import com.scolley.betterlife.R
import com.scolley.betterlife.data.Groups
import com.scolley.betterlife.data.Plan
import com.scolley.betterlife.data.Result
import com.scolley.betterlife.data.User
import com.scolley.betterlife.data.source.PlanRepository
import com.scolley.betterlife.newwork.LoadApiStatus
import com.scolley.betterlife.util.Logger
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AddTaskViewModel(private val repository: PlanRepository) : ViewModel() {

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

        partner.value = ""
        name.value = ""
        dailyTarget.value = 0
        target.value = 1
        dueDate.value = -1
        isGroup.value = false
        selectedTypeRadio.value = 0

        findAllUser()
    }

    fun navigateToHome() {
        _navigateToHome.value = true
    }

    private fun navigateToHomeAfterSend() {
        _navigateToHome.value = true
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

    private fun findAllUser() {

        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            val result = repository.findAllUser()

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
                }
            }
            _refreshStatus.value = false
        }

    }

    fun convertUserDataToArray() {

        val listName = mutableListOf<String>()
        val listID = mutableListOf<String>()
        users.value?.let {
            it.indices.let { indices ->
                for (i in indices) {
                    if (it[i]!!.userId != FirebaseAuth.getInstance().currentUser!!.uid) {
                        listName.run {
                            add(it[i]!!.userName)
                        }
                        listID.run {
                            add(it[i]!!.userId)
                        }
                    }
                }
            }
        }
        userName.value = listName
        userID.value = listID
    }


    fun isUserExist() {

        coroutineScope.launch {

            if (isGroup.value == true) {
                val userIsEmpty: Boolean = when (val result = repository.findUserByName(partner.value!!)) {
                    is Result.Success -> {
                        _error.value = null
                        _status.value = LoadApiStatus.DONE
                        result.data
                    }
                    is Result.Fail -> {
                        _error.value = result.error
                        _status.value = LoadApiStatus.ERROR
                        false
                    }
                    is Result.Error -> {
                        _error.value = result.exception.toString()
                        _status.value = LoadApiStatus.ERROR
                        false
                    }
                    else -> {
                        _error.value = PlanApplication.instance.getString(R.string.you_know_nothing)
                        _status.value = LoadApiStatus.ERROR
                        false
                    }
                }

                if (userIsEmpty) {
                    Toast.makeText(
                            PlanApplication.appContext,
                            PlanApplication.instance.getString(R.string.text_NoUser),
                            Toast.LENGTH_SHORT
                    ).show()
                } else {
                    addTask()
                }
            } else {
                addTask()
            }
        }

    }

    private fun setNewGroup(partnerID: String, taskID: String) {

        coroutineScope.launch {

            if (isGroup.value == true) {
                val newGroup = Groups(
                        membersID = listOf(FirebaseAuth.getInstance().currentUser!!.uid, partnerID)
                )
                when (val result = repository.addGroup(newGroup, taskID)) {
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

        }
    }


    private fun addTask() {

        coroutineScope.launch {

            var partnerID: String = ""

            if (isGroup.value == true) {
                for (i in users.value!!.indices) {
                    if (users.value!![i]!!.userName == partner.value) {
                        partnerID = users.value!![i]!!.userId
                        break
                    }
                }
            }

            val newTask = Plan(
                    members = if (isGroup.value == false) {
                        listOf(FirebaseAuth.getInstance().currentUser!!.uid)
                    } else {
                        listOf(FirebaseAuth.getInstance().currentUser!!.uid, partnerID)
                    },
                    name = name.value!!,
                    dailyTarget = dailyTarget.value!!,
                    category = category.value!!,
                    target = target.value!!.toInt(),
                    dueDate = dueDate.value!!,
                    group = isGroup.value!!
            )

            var taskID: String = when (val result = repository.addTask(newTask)) {
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

            setNewGroup(partnerID, taskID)

            navigateToHomeAfterSend()

        }
    }

}
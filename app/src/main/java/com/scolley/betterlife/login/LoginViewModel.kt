package com.scolley.betterlife.login

import android.icu.util.Calendar
import android.text.format.DateFormat
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.scolley.betterlife.PlanApplication
import com.scolley.betterlife.R
import com.scolley.betterlife.data.Result
import com.scolley.betterlife.data.User
import com.scolley.betterlife.data.source.PlanRepository
import com.scolley.betterlife.util.Util.getString
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class LoginViewModel(private val repository: PlanRepository) : ViewModel() {

    private val _user = MutableLiveData<User>()

    val user: LiveData<User>
        get() = _user

    // Handle leave login
    private val _navigateToHome = MutableLiveData<Boolean>()

    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val auth = FirebaseAuth.getInstance()

    private val _loginAttempt = MutableLiveData<Boolean>()

    val loginAttempt: LiveData<Boolean>
        get() = _loginAttempt

    fun afterLogin() {
        _loginAttempt.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun findUser(firebaseUser: FirebaseUser, firstLogin: Boolean) {
        coroutineScope.launch {
            val result = repository.findUser(firebaseUser.uid)
            UserManager.userID = firebaseUser.uid
            UserManager.userName = firebaseUser.displayName
            _user.value = when (result) {
                is Result.Success -> {
                    if (result.data != null) {
                        if (!firstLogin) {
                            loginSuccess()
                        }
                        result.data
                    } else {
                        val newUser = User(
                                userId = firebaseUser.uid,
                                userName = firebaseUser.displayName
                                        ?: getString(R.string.login_name_unknown),
                                userImage = firebaseUser.photoUrl.toString()

                        )
                        createUser(newUser, firstLogin)
                        null
                    }
                }
                is Result.Fail -> {
                    null
                }
                is Result.Error -> {
                    null
                }
                else -> {
                    null
                }
            }
        }
    }

    private fun createUser(user: User, firstLogin: Boolean) {
        coroutineScope.launch {
            _user.value = when (repository.createUser(user)) {
                is Result.Success -> {
                    if (!firstLogin) {
                        loginSuccess()
                    }
                    user
                }
                is Result.Fail -> {
                    null
                }
                is Result.Error -> {
                    null
                }
                else -> {
                    null
                }
            }
        }
    }

    fun loginGoogle() {
        if (_user.value != null) {
            loginSuccess()
        } else {
            _loginAttempt.value = true
        }
    }

    private fun loginSuccess() {
        coroutineScope.launch {
            val date =
                    DateFormat.format(
                            PlanApplication.instance.getString(R.string.diary_select_date),
                            Date(Calendar.getInstance().timeInMillis)
                    ).toString()
                    UserManager.lastTimeGoogle = date

            navigateToHome()
        }
    }

    private fun navigateToHome() {
        _navigateToHome.value = true
        Toast.makeText(
                PlanApplication.appContext,
                PlanApplication.instance.getString(R.string.login_success),
                Toast.LENGTH_SHORT
        ).show()
    }

    fun onSucceeded() {
        _navigateToHome.value = null
    }




    fun firebaseAuthWithGoogle(idToken: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(idToken.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(LoginFragment.TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(LoginFragment.TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {

    }
}
package com.reysand.easypay.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reysand.easypay.data.EasyPayService
import com.reysand.easypay.data.LoginRequest
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResult?>()
    val loginResult: LiveData<LoginResult?> = _loginResult

    fun performLogin(easyPayService: EasyPayService, username: String, password: String) {
        Log.d("LoginViewModel", "Perform login: $username / $password")
        viewModelScope.launch {
            val request = LoginRequest(username, password)
            try {
                val response = easyPayService.login(request)

                if (response.isSuccessful) {
                    if (response.body()?.success != true) {
                        _loginResult.value =
                            LoginResult.Error(response.body()?.error?.message ?: "Login failed")
                    } else {
                        val token = response.body()!!.response?.token
                        _loginResult.value = LoginResult.Success(token)
                        Log.d("LoginViewModel", "Token: $token")
                        Log.d("LoginViewModel", "Response success: ${response.body()}")
                    }
                } else {
                    _loginResult.value = LoginResult.Error("Response failed")
                }
            } catch (e: Exception) {
                _loginResult.value = LoginResult.Error(e.message.toString())
            }
        }
    }

    fun logout() {
        _loginResult.value = null
    }
}

sealed class LoginResult {
    data class Success(val token: String?) : LoginResult()
    data class Error(val message: String) : LoginResult()
}
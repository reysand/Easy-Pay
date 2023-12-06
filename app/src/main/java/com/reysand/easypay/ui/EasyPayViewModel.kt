package com.reysand.easypay.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.reysand.easypay.data.EasyPayService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EasyPayViewModel : ViewModel() {

    private val _currentLogin = MutableLiveData<Map<String, String>?>()
    val currentLogin: LiveData<Map<String, String>?> = _currentLogin

    // Retrofit service
    val easyPayService: EasyPayService by lazy {
        Retrofit.Builder()
            .baseUrl("https://easypay.world/api-test/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EasyPayService::class.java)
    }

    fun setCurrentLogin(username: String, token: String) {
        _currentLogin.value = mapOf(username to token)
    }

    fun resetCurrentLogin() {
        _currentLogin.value = null
    }

    companion object {
        fun provideFactory(): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(EasyPayViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return EasyPayViewModel() as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}
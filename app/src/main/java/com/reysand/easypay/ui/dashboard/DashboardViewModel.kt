package com.reysand.easypay.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reysand.easypay.data.Payment
import com.reysand.easypay.ui.EasyPayViewModel
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {

    private val _paymentListResult = MutableLiveData<PaymentListResult>()
    val paymentListResult: LiveData<PaymentListResult> = _paymentListResult

    fun fetchPayments(easyPayViewModel: EasyPayViewModel, token: String) {
        viewModelScope.launch {
            try {
                val response = easyPayViewModel.easyPayService.getPayments(token)
                if (response.isSuccessful) {
                    if (response.body()?.success != true) {
                        _paymentListResult.value =
                            PaymentListResult.Error(response.body().toString())
                    } else {
                        Log.d("DashboardViewModel", "Response success: ${response.body()}")
                        _paymentListResult.value =
                            PaymentListResult.Success(response.body()!!.response)
                    }
                } else {
                    _paymentListResult.value = PaymentListResult.Error("Response failed")
                    Log.d("DashboardViewModel", "Response failed: ${response.body()}")
                }
            } catch (e: Exception) {
                _paymentListResult.value = PaymentListResult.Error(e.message.toString())
                e.printStackTrace()
            }
        }
    }
}

sealed class PaymentListResult {
    data class Success(val paymentList: List<Payment>?) : PaymentListResult()
    data class Error(val message: String) : PaymentListResult()
}
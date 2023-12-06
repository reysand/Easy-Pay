package com.reysand.easypay.data

data class PaymentListResponse(
    val success: Boolean,
    val response: List<Payment>?,
    val error: Error?
)

data class Payment(
    val id: Int,
    val title: String,
    val amount: String? = "0.0",
    val created: Long = 0L
)

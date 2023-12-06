package com.reysand.easypay.data

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val success: Boolean,
    val response: Token?,
    val error: Error?
)

data class Token(
    val token: String
)

data class Error(
    @SerializedName("error_code")
    val code: Int,
    @SerializedName("error_msg")
    val message: String
)

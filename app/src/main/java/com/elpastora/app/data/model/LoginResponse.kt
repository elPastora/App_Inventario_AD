package com.elpastora.app.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("usuario") val usuario: Usuario,
    @SerializedName("token") val token: String
)
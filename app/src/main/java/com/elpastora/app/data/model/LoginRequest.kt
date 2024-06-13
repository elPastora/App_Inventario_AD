package com.elpastora.app.data.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("nombreUsuario") val nombreUsuario: String,
    @SerializedName("contraseña") val contraseña: String
)

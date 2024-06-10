package com.elpastora.app.data.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Usuario(
    @SerializedName("nombreUsuario") val nombreUsuario: String,
    @SerializedName("email") val email: String,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("apellido") val apellido: String,
    @SerializedName("fechaNacimiento") val fechaNacimiento: Date,
    @SerializedName("rol") val rol: String
)
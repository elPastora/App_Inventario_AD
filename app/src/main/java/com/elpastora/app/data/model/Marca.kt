package com.elpastora.app.data.model

import com.google.gson.annotations.SerializedName

data class Marca(
    @SerializedName("idMarca") val idMarca: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("descripcion") val descripcion: String
)
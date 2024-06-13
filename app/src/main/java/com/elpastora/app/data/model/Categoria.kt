package com.elpastora.app.data.model

import com.google.gson.annotations.SerializedName

data class Categoria(
    @SerializedName("idCategoria") val idCategoria: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("descripcion") val descripcion: String
)
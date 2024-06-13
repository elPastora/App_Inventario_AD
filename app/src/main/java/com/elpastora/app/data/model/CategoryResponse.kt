package com.elpastora.app.data.model

import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("categorias") val categorias: List<Categoria>
)
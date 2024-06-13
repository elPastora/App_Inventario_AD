package com.elpastora.app.data.model

import com.google.gson.annotations.SerializedName

data class BrandResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("marcas") val marcas: List<Marca>
)
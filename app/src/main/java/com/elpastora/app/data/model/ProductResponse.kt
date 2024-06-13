package com.elpastora.app.data.model

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("producto") val producto: Producto
)

data class ProductsResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("productos") val productos: List<Producto>
)
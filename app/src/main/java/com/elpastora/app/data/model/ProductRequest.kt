package com.elpastora.app.data.model

import com.google.gson.annotations.SerializedName

data class ProductRequest(
    @SerializedName("codigoProducto") val codigoProducto: String,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("idCategoria") val idCategoria: Int,
    @SerializedName("idMarca") val idMarca: Int,
    @SerializedName("cantidad") val cantidad: Int,
    @SerializedName("cantidadReserva") val cantidadReserva: Int,
    @SerializedName("precioCompra") val precioCompra: Double,
    @SerializedName("precioVenta") val precioVenta: Double
)
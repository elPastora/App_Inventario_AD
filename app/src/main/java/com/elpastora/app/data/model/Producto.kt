package com.elpastora.app.data.model

import com.google.gson.annotations.SerializedName

data class Producto(
    @SerializedName("codigoProducto") val codigoProducto: String,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("idCategoria") val idCategoria: Int,
    @SerializedName("nombreCategoria") val nombreCategoria: String,
    @SerializedName("idMarca") val idMarca: Int,
    @SerializedName("nombreMarca") val nombreMarca: String,
    @SerializedName("cantidad") val cantidad: Int,
    @SerializedName("cantidadReserva") val cantidadReserva: Int,
    @SerializedName("precioCompra") val precioCompra: Int,
    @SerializedName("precioVenta") val precioVenta: Int,
    @SerializedName("imagen") val imagen: String
)
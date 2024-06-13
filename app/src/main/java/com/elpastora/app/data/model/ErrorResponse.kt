package com.elpastora.app.data.model

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("errors") val errors: List<MessageError>
)

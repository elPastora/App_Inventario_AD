package com.elpastora.app.data.api

import com.elpastora.app.data.model.LoginRequest
import com.elpastora.app.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {

    @POST("auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>
}
package com.elpastora.app.data.api

import com.elpastora.app.data.model.BrandResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface BrandService {

    @GET("marcas")
    suspend fun getAllBrands(@Header(value = "Authorization") authHeader: String) : Response<BrandResponse>
}
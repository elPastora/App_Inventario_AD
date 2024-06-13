package com.elpastora.app.data.api

import com.elpastora.app.data.model.ProductRequest
import com.elpastora.app.data.model.ProductResponse
import com.elpastora.app.data.model.ProductsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ProductService {

    @GET("productos")
    suspend fun getAllProducts(@Header(value = "Authorization") authHeader: String) : Response<ProductsResponse>

    @POST("productos")
    suspend fun addProducts(@Header(value = "Authorization") authHeader: String, @Body productRequest: ProductRequest) : Response<ProductResponse>
}
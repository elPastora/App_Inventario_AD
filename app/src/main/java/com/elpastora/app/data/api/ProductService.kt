package com.elpastora.app.data.api

import com.elpastora.app.data.model.ProductRequest
import com.elpastora.app.data.model.ProductResponse
import com.elpastora.app.data.model.ProductsResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ProductService {

    @GET("productos")
    suspend fun getAllProducts(@Header(value = "Authorization") authHeader: String) : Response<ProductsResponse>

    @Multipart
    @POST("productos")
    suspend fun addProducts(
        //@Header(value = "Authorization") authHeader: String,
        @Part image: MultipartBody.Part
    ) : Response<ProductResponse>


    @GET("productos/{id}")
    suspend fun getProductById(@Header(value = "Authorization") authHeader: String, @Path("id") productId: String) : Response<ProductResponse>

    @PUT("productos/{id}")
    suspend fun updateProduct(@Header(value = "Authorization") authHeader: String, @Path("id") productId: String, @Body productRequest: ProductRequest) : Response<ProductResponse>
}
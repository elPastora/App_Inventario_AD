package com.elpastora.app.data.api

import com.elpastora.app.data.model.CategoryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface CategoryService {

    @GET("categorias")
    suspend fun getAllCategories(@Header(value = "Authorization") authHeader: String) : Response<CategoryResponse>
}
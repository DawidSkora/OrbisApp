package com.example.orbisapp.network

import com.example.orbisapp.model.QAPair
import retrofit2.http.*

interface ApiService {

    @GET("qa/")
    suspend fun getAll(): List<QAPair>

    @POST("qa/")
    suspend fun create(@Body pair: QAPair): QAPair

    @PUT("qa/{id}")
    suspend fun update(@Path("id") id: String, @Body pair: QAPair): QAPair

    @DELETE("qa/{id}")
    suspend fun delete(@Path("id") id: String)
}
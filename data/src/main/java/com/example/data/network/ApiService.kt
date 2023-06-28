package com.example.data.network

import com.example.data.network.models.ToDoItemDto
import com.example.data.network.models.ToDoListDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.UUID

interface ApiService {

    @Headers("Authorization: Bearer grassed")
    @GET("list")
    suspend fun getToDoList(): ToDoListDto

    @Headers("X-Last-Known-Revision: 0", "Authorization: Bearer grassed")
    @POST("list")
    suspend fun uploadToDoElement(@Body toDoItemDto: ToDoItemDto)


    @GET("list/{id}")
    suspend fun getToDoItemById(@Path("id") id: Int): ToDoListDto


    companion object {
        const val token = "y0_AgAAAABEww8dAAoW1AAAAADmPgw8qnY-Ic0-RbqS2v0vK-8qtP2eaFM"
    }
}
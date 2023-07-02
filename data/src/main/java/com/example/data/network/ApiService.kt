package com.example.data.network

import com.example.data.network.models.PatchToDoListDto
import com.example.data.network.models.ToDoItemDto
import com.example.data.network.models.ToDoListDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.UUID

interface ApiService {

    @GET("list")
    suspend fun getToDoList(): Response<ToDoListDto>


    @POST("list")
    suspend fun addToDo(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body toDoItemDto: ToDoItemDto
    ): Response<ToDoItemDto>


    @GET("list/{id}")
    suspend fun getToDoById(
        @Path("id") id: Int): Response<ToDoItemDto>


    @DELETE("list/{id}")
    suspend fun deleteTodoById(
        @Path("id") id: UUID
    ): Response<ToDoItemDto>


    @PATCH("list")
    suspend fun updateToDoList(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body toDoList: PatchToDoListDto
    ): Response<ToDoListDto>

}
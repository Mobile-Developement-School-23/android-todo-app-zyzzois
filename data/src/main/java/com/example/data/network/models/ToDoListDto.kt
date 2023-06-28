package com.example.data.network.models

import com.google.gson.annotations.SerializedName

data class ToDoListDto(

    @SerializedName("list")
    val list: List<ToDoItemDto>,

    @SerializedName("revision")
    val revision: Int,

    @SerializedName("status")
    val status: String
)
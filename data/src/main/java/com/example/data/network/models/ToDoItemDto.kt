package com.example.data.network.models

import com.google.gson.annotations.SerializedName

data class ToDoItemDto(

    @SerializedName("element")
    val elementDto: ElementDto,

    @SerializedName("revision")
    val revision: Int,

    @SerializedName("status")
    val status: String

)
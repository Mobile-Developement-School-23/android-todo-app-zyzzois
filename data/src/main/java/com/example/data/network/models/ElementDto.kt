package com.example.data.network.models

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class ElementDto(

    @SerializedName("id")
    val id: String = UUID.randomUUID().toString(),

    @SerializedName("text")
    val text: String,

    @SerializedName("importance")
    val importance: String,

    @SerializedName("deadline")
    val deadline: Long,

    @SerializedName("done")
    val done: Boolean,

    @SerializedName("color")
    val color: String? = null,

    @SerializedName("created_at")
    val created_at: Long = 1L,

    @SerializedName("changed_at")
    val changed_at: Long = 1L,

    @SerializedName("last_updated_by")
    val last_updated_by: String = "123",
)
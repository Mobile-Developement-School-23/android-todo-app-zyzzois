package com.example.data.network.models

import com.google.gson.annotations.SerializedName

data class PatchToDoListDto (
    @SerializedName("list")
    val list: List<ElementDto>
)
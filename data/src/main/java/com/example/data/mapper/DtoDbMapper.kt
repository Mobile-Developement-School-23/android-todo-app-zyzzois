package com.example.data.mapper

import com.example.data.database.TodoItemModelDb
import com.example.data.network.models.ElementDto
import com.example.data.network.models.PatchToDoListDto
import java.util.UUID
import javax.inject.Inject

class DtoDbMapper @Inject constructor() {

    fun mapListModelDbToListDto(listModelDb: List<TodoItemModelDb>) = PatchToDoListDto(
        listModelDb.map {
            ElementDto(
                id = UUID.randomUUID().toString(),
                text = it.text,
                deadline = 9L,
                importance = "low",
                done = it.completed,
                last_updated_by = "me"
            )
        }
    )

}
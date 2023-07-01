package com.example.data.mapper

import com.example.data.database.TodoItemModelDb
import com.example.data.network.models.ElementDto
import com.example.data.network.models.PatchToDoListDto
import com.example.data.network.models.ToDoListDto
import javax.inject.Inject

class DtoDbMapper @Inject constructor() {

    fun mapListModelDbToListDto(listModelDb: List<TodoItemModelDb>) = PatchToDoListDto(
        listModelDb.map {
            ElementDto(
                id = it.id.toString(),
                text = it.text,
                deadline = it.deadline,
                importance = "low",
                done = it.completed,
                last_updated_by = "me"
            )
        }
    )

    fun mapListDtoToListModelDb(listDto: ToDoListDto): List<TodoItemModelDb> {
        val res = listDto.list.map {
            TodoItemModelDb(
                id = it.elementDto.id.toInt(),
                text = it.elementDto.text,
                importance = it.elementDto.importance,
                deadline = it.elementDto.deadline,
                completed = it.elementDto.done,
                dateOfCreation = it.elementDto.created_at,
                dateOfChange = it.elementDto.changed_at
            )
        }
        return res
    }

}
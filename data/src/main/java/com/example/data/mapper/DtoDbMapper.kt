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
                importance = it.importance,
                done = it.completed,
                last_updated_by = "me",
                created_at = it.dateOfCreation,
                changed_at = it.dateOfChange
            )
        }
    )

    fun mapListDtoToListModelDb(listDto: ToDoListDto): List<TodoItemModelDb> {
        val res = listDto.list.map {
            TodoItemModelDb(
                id = it.id.toInt(),
                text = it.text,
                importance = it.importance,
                deadline = it.deadline,
                completed = it.done,
                dateOfCreation = it.created_at,
                dateOfChange = it.changed_at
            )
        }
        return res
    }

    fun mapListDtoToListModelDb(listDto: List<ElementDto>): List<TodoItemModelDb> {
        val res = listDto.map {
            TodoItemModelDb(
                id = it.id.toInt(),
                text = it.text,
                importance = it.importance,
                deadline = it.deadline,
                completed = it.done,
                dateOfCreation = it.created_at,
                dateOfChange = it.changed_at
            )
        }
        return res
    }

    fun mapDbModelToDto(modelDb: TodoItemModelDb) = ElementDto(
        id = modelDb.id.toString(),
        text = modelDb.text,
        importance = modelDb.importance,
        deadline = modelDb.deadline,
        done = modelDb.completed,
        created_at = modelDb.dateOfCreation,
        changed_at = modelDb.dateOfChange,
        last_updated_by = "me"
    )


}

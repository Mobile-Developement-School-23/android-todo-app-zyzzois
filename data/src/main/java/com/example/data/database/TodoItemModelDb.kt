package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Класс TodoItemModelDb представляет модель данных для элемента списка задач,
 * которая будет сохранена в базе данных.
 * Свойства:
 * id: Уникальный идентификатор элемента списка задач.
 * text: Текст задачи.
 * importance: Важность задачи.
 * deadline: Срок выполнения задачи.
 * completed: Флаг, указывающий, выполнена ли задача.
 * dateOfCreation: Дата создания задачи.
 * dateOfChange: Дата последнего изменения задачи.
 * Класс TodoItemModelDb отмечен аннотацией @Entity(tableName = "todos"),
 * что указывает Room, что он должен быть сохранен в базе данных с таблицей "todos".
 * Ключевое поле id помечено аннотацией @PrimaryKey(autoGenerate = true), чтобы указать,
 * что это поле является первичным ключом и будет автоматически генерироваться
 * базой данных при вставке нового элемента. Вы можете использовать эту модель данных для
 * сохранения и получения информации о задачах в базе данных.
*/

@Entity(tableName = "todos")
data class TodoItemModelDb(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val text: String,
    val importance: String,
    val deadline: Long,
    val completed: Boolean,
    val dateOfCreation: Long,
    val dateOfChange: Long
)

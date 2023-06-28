package com.example.todo.ui.screens.detail

import com.example.domain.entity.TodoItemEntity

sealed class State

object Progress: State()
object Error: State()
class Result(toDo: TodoItemEntity): State()
//object EditMode: State()
//object AddMode: State()
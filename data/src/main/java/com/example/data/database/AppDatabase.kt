package com.example.data.database

/**
 * Класс AppDatabase является подклассом RoomDatabase и служит основной точкой доступа к
 * SQLite-базе данных приложения. Он определяет конфигурацию базы данных и предоставляет
 * методы для доступа к объектам доступа к данным (DAO).
 * Методы: todoDao(): Возвращает экземпляр класса ToDoDao, который используется
 * для доступа и обработки данных TodoItemModelDb.
 * Компаньон-объект:
 * Компаньон-объект содержит следующие методы:
 * getInstance(context: Context): Возвращает синглтон-экземпляр класса AppDatabase.
 * Если экземпляр не существует, создается новый. Принимает контекст приложения в качестве параметра.
 * Свойства:
 * DB_NAME: Константное свойство, содержащее имя файла SQLite-базы данных ("database.db").
 * Использование:
 * Для использования класса AppDatabase можно вызвать метод getInstance(context), передавая контекст
 * приложения в качестве аргумента. Это предоставит вам экземпляр класса AppDatabase,
 * с помощью которого вы можете получить доступ к ToDoDao и выполнять операции с базой данных.
 */

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TodoItemModelDb::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun todoDao(): ToDoDao

    companion object {
        private var db: AppDatabase? = null
        private const val DB_NAME = "database.db"
        private val LOCK = Any()

        fun getInstance(context: Context): AppDatabase {
            synchronized(LOCK) {
                db?.let { return it }
                val instance =
                    Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        DB_NAME
                    ).build()
                db = instance
                return instance
            }
        }
    }
}

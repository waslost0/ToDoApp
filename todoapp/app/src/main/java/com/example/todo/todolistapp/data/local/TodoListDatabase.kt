package com.example.todo.todolistapp.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

//You can also check out type converters
@Database(entities = [Todo::class], version = 1, exportSchema = false)
abstract class TodoListDatabase: RoomDatabase(){


    abstract fun getTodoDao(): TodoDao


    companion object {
        val databaseName = "tododatabase"
        var todoListDatabase: TodoListDatabase? = null


        fun getInstance(context: Context): TodoListDatabase?{
            if (todoListDatabase == null) {

                synchronized(TodoListDatabase::class) {

                    todoListDatabase = Room.databaseBuilder(context,
                            TodoListDatabase::class.java,
                            databaseName)
                            .build()
                }
            }


            return todoListDatabase
        }
    }
}
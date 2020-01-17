package com.example.todo.todolistapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TodoDao{

    @Insert
    fun saveTodo(todo: Todo)

    @Update
    fun updateTodo(todo: Todo)


    @Delete
    fun removeTodo(todo: Todo)

    @Query("SELECT*FROM todo ORDER BY id ASC")
    fun getTodoList(): LiveData<List<Todo>>

//    @Query("SELECT*FROM todo WHERE tId=:tid")
//    fun getTodoItem(tid: Int): Todo
}
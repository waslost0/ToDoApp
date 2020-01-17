package com.example.todo.todolistapp.data.local

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData

class NoteRepository(application: Application) {

    private var noteDao: TodoDao

    private var allNotes: LiveData<List<Todo>>

    init {
        val database: TodoListDatabase = TodoListDatabase.getInstance(
            application.applicationContext
        )!!
        noteDao = database.getTodoDao()
        allNotes = noteDao.getTodoList()
    }

    fun insert(note: Todo) {
        InsertNoteAsyncTask(noteDao).execute(note)
    }

    fun update(note: Todo) {
        UpdateNoteAsyncTask(noteDao).execute(note)
    }

    fun delete(note: Todo) {
        DeleteNoteAsyncTask(noteDao).execute(note)
    }

    fun getAllNotes(): LiveData<List<Todo>> {
        return allNotes
    }

    companion object { // ??
        private class InsertNoteAsyncTask(val noteDao: TodoDao) : AsyncTask<Todo, Unit, Unit>() {

            override fun doInBackground(vararg p0: Todo?) {
                noteDao.saveTodo(p0[0]!!)
            }
        }

        private class UpdateNoteAsyncTask(val noteDao: TodoDao) : AsyncTask<Todo, Unit, Unit>() {

            override fun doInBackground(vararg p0: Todo?) {
                noteDao.updateTodo(p0[0]!!)
            }
        }

        private class DeleteNoteAsyncTask(val noteDao: TodoDao) : AsyncTask<Todo, Unit, Unit>() {

            override fun doInBackground(vararg p0: Todo?) {
                noteDao.removeTodo(p0[0]!!)
            }
        }

    }
}
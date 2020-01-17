package com.example.todo.todolistapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.todo.todolistapp.data.local.NoteRepository
import com.example.todo.todolistapp.data.local.Todo


class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: NoteRepository =
            NoteRepository(application)
    private var allNotes: LiveData<List<Todo>> = repository.getAllNotes()

    fun insert(note: Todo) {
        repository.insert(note)
    }

    fun update(note: Todo) {
        repository.update(note)
    }

    fun delete(note: Todo) {
        repository.delete(note)
    }

    fun getAllNotes(): LiveData<List<Todo>> {
        return allNotes
    }
}
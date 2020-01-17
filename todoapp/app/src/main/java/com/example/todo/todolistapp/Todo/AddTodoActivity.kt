package com.example.todo.todolistapp.Todo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.view.WindowManager
import android.widget.RadioGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.example.todo.todolistapp.R
import com.example.todo.todolistapp.data.local.TodoListDatabase
import com.example.todo.todolistapp.data.local.Todo
import com.example.todo.todolistapp.viewmodels.NoteViewModel
import kotlinx.android.synthetic.main.activity_add_todo.*
import kotlin.math.log

class AddTodoActivity : AppCompatActivity(), RadioGroup.OnCheckedChangeListener {

    companion object {
        const val EXTRA_ID = "com.example.todo.todolistapp.EXTRA_ID"
        const val EXTRA_TITLE = "com.example.todo.todolistapp.EXTRA_TITLE"
        const val EXTRA_DESCRIPTION = "com.example.todo.todolistapp.EXTRA_DESCRIPTION"
        const val EXTRA_PRIORITY = "com.example.todo.todolistapp.EXTRA_PRIORITY"
    }

    private var todoDatabase: TodoListDatabase? = null
    private var priority = 1
    private var updateF = false
    private lateinit var noteViewModel: NoteViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_todo)

        showKeyboard()

        todoDatabase = TodoListDatabase.getInstance(this)
        radioGroup.setOnCheckedChangeListener(this)

        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("detail")
        val priority = intent.getIntExtra("priority", 0)


        if (intent.hasExtra("id")) {
            newTask.text = "Update Task"
            add_todo.text = "Update todo"
            detail_ed.setText(description)
            title_ed.setText(title)
            if (priority == 1) low.isChecked = true
            if (priority == 2) medium.isChecked = true
            if (priority == 3) high.isChecked = true
            updateF = true
        }

        add_todo.setOnClickListener {
            saveNote()
        }

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)
    }

    private fun saveNote() {
        if (detail_ed.text.toString().trim().isBlank() || title_ed.text.toString().trim().isBlank()) {
            Toast.makeText(this, "Can not insert empty note!", Toast.LENGTH_SHORT).show()
        } else {

            val id = intent.getIntExtra("id", 0)
            val todo = Todo(title_ed.text.toString(), priority)
            todo.description = detail_ed.text.toString()

            if (updateF){
                todo.id = id
                noteViewModel.update(todo)

            } else {
                noteViewModel.insert(todo)
            }
            finish()
        }

    }

    private fun showKeyboard() {
        title_ed.requestFocus()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
    }


    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        if (checkedId == R.id.medium) {
            priority = 2
        } else if (checkedId == R.id.high) {
            priority = 3
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            startActivity(Intent(this, TodoActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

}
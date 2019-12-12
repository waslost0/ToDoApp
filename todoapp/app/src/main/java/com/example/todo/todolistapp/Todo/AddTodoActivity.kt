package com.example.todo.todolistapp.Todo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.view.WindowManager
import android.widget.RadioGroup
import com.example.todo.todolistapp.R
import com.example.todo.todolistapp.data.local.TodoListDatabase
import com.example.todo.todolistapp.data.local.models.Todo
import kotlinx.android.synthetic.main.activity_add_todo.*

class AddTodoActivity: AppCompatActivity(), RadioGroup.OnCheckedChangeListener{

    private var todoDatabase: TodoListDatabase? = null
    private var priority = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_todo)

        showKeyboard()

        todoDatabase = TodoListDatabase.getInstance(this)
        radioGroup.setOnCheckedChangeListener(this)

        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("detail")

        if (title == null || title == ""){
            add_todo.setOnClickListener{
                val todo = Todo(title_ed.text.toString(), priority)
                todo.detail = detail_ed.text.toString()
                todoDatabase!!.getTodoDao().saveTodo(todo)
                finish()
            }
        }else{
            newTask.text = "Update Task"
            add_todo.text = getString(R.string.update)
            val tId = intent.getIntExtra("tId", 0)
            title_ed.setText(title)

            detail_ed.setText(description)

            add_todo.setOnClickListener {
                val todo = Todo(title_ed.text.toString(), priority, tId)
                todo.detail = detail_ed.text.toString()
                todoDatabase!!.getTodoDao().updateTodo(todo)
                finish()
            }
        }
    }

    private fun showKeyboard() {
        title_ed.requestFocus()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
    }


    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        if (checkedId == R.id.medium){
            priority = 2
        }else if (checkedId == R.id.high) {
            priority = 3
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home){
            startActivity(Intent(this, TodoActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

}
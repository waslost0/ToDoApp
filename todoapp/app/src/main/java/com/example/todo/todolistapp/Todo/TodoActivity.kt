package com.example.todo.todolistapp.Todo
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.todolistapp.R
import com.example.todo.todolistapp.data.local.TodoListDatabase
import com.example.todo.todolistapp.data.local.models.Todo
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_toast.*


class TodoActivity : AppCompatActivity(), TodoAdapter.OnTodoItemClickedListener, View.OnTouchListener{

    private var todoDatabase: TodoListDatabase? = null
    private var todoAdapter: TodoAdapter? = null
    var dX = 0f
    var dY = 0f
    var lastAction = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        todoDatabase = TodoListDatabase.getInstance(this)
        todoAdapter = TodoAdapter()
        todoAdapter?.setTodoItemClickedListener(this)

        add_todo.setOnTouchListener(this)
    }

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                dX = view.x - event.rawX
                dY = view.y - event.rawY
                lastAction = MotionEvent.ACTION_DOWN
            }
            MotionEvent.ACTION_MOVE -> {
                view.y = event.rawY + dY
                view.x = event.rawX + dX
                lastAction = MotionEvent.ACTION_MOVE
            }
            MotionEvent.ACTION_UP -> {
                if (lastAction === MotionEvent.ACTION_DOWN)
                    startActivity(Intent(this, AddTodoActivity::class.java))
            }
            else -> return false
        }
        return true
    }


    override fun deleteItem(todo: Todo) {
        todoDatabase?.getTodoDao()?.removeTodo(todo)
        onResume()

        val snackbar = Snackbar.make(
                findViewById(R.id.top_coordinator),
                " removed from Recyclerview!",
                Snackbar.LENGTH_LONG
        )
        snackbar.setAction("UNDO") {
            restoreItem(todo)
        }
        snackbar.setActionTextColor(Color.YELLOW)
        snackbar.show()

    }

    override fun restoreItem(todo: Todo) {
        todoDatabase!!.getTodoDao().saveTodo(todo)
        onResume()
    }

    fun showToast(){
        val layout = layoutInflater.inflate(R.layout.custom_toast, linearLayout)
        val myToast = Toast(applicationContext)
        myToast.duration = Toast.LENGTH_LONG
        myToast.view = layout
        myToast.show()
    }

    override fun onResume() {
        super.onResume()

        todoAdapter?.todoList = todoDatabase?.getTodoDao()?.getTodoList()

        todo_rv.adapter = todoAdapter
        todo_rv.layoutManager = LinearLayoutManager(this)
        todo_rv.hasFixedSize()
    }


    override fun onTodoItemClicked(todo: Todo) {
        val intent = Intent(this, AddTodoActivity::class.java)
        intent.putExtra("tId", todo.tId)
        intent.putExtra("title", todo.title)
        intent.putExtra("priority", todo.priority)
        intent.putExtra("detail", todo.detail)
        startActivity(intent)
    }

    override fun onTodoItemLongClicked(todo: Todo) {
        val alertDialog = AlertDialog.Builder(this)
                .setItems(R.array.dialog_list, DialogInterface.OnClickListener { dialog, which ->
                    if (which==0){
                        val intent = Intent(this@TodoActivity, AddTodoActivity::class.java)
                        intent.putExtra("tId", todo.tId)
                        intent.putExtra("title", todo.title)
                        intent.putExtra("priority", todo.priority)
                        intent.putExtra("detail", todo.detail)
                        startActivity(intent)
                    }else{
                        todoDatabase?.getTodoDao()?.removeTodo(todo)
                        onResume()
                    }
                    dialog.dismiss()
                })
                .create()
        alertDialog.show()
    }
}



package com.example.todo.todolistapp.Todo

import android.media.Image
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.todo.todolistapp.R
import com.example.todo.todolistapp.data.local.Todo
import kotlinx.android.synthetic.main.todo_item_view.view.*


class TodoAdapter : ListAdapter<Todo, TodoAdapter.NoteHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Todo>() {
            override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
                return oldItem.title == newItem.title && oldItem.description == newItem.description
                        && oldItem.priority == newItem.priority
            }
        }
    }

    private var listener: OnItemClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.todo_item_view, parent, false)
        return NoteHolder(itemView)
    }


    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        val currentNote: Todo = getItem(position)

        holder.textViewTitle.text = currentNote.title

        holder.textViewPriority.setImageResource(getImage(currentNote.priority))

        holder.textViewDescription.text = currentNote.description
    }

    fun getNoteAt(position: Int): Todo {
        return getItem(position)
    }

    inner class NoteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(getItem(position))
                }
            }

            itemView.findViewById<ImageView>(R.id.deleteTask).setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onDeleteItemClick(getItem(position))
                }
            }
        }

        var textViewTitle: TextView = itemView.title
        var textViewPriority: ImageView = itemView.priority_imgView
        var textViewDescription: TextView = itemView.description

    }

    private fun getImage(priority: Int): Int =
            when (priority) {
                1 -> R.drawable.low_priority
                2 -> R.drawable.medium_priority
                else -> R.drawable.high_priority
            }

    interface OnItemClickListener {
        fun onItemClick(note: Todo)
        fun onDeleteItemClick(note: Todo)

    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

}
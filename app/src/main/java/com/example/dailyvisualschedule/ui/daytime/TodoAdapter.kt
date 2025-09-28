package com.example.dailyvisualschedule.ui.daytime

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyvisualschedule.databinding.ListItemTodoBinding

class TodoAdapter(
    private val todoItems: MutableList<TodoItem>
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ListItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todoItem = todoItems[position]
        holder.bind(todoItem)
    }

    override fun getItemCount(): Int = todoItems.size

    inner class TodoViewHolder(private val binding: ListItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(todoItem: TodoItem) {
            binding.imageViewTaskIcon.setImageResource(todoItem.imageResId)

            // Ellie's star
            if (todoItem.isEllieCompleted) {
                binding.buttonStarEllie.setImageResource(android.R.drawable.btn_star_big_on)
            } else {
                binding.buttonStarEllie.setImageResource(android.R.drawable.btn_star_big_off)
            }
            binding.buttonStarEllie.setOnClickListener {
                todoItem.isEllieCompleted = !todoItem.isEllieCompleted
                notifyItemChanged(adapterPosition) // Or notifyItemChanged(position) if adapterPosition is -1
            }

            // Ada's star
            if (todoItem.isAdaCompleted) {
                binding.buttonStarAda.setImageResource(android.R.drawable.btn_star_big_on)
            } else {
                binding.buttonStarAda.setImageResource(android.R.drawable.btn_star_big_off)
            }
            binding.buttonStarAda.setOnClickListener {
                todoItem.isAdaCompleted = !todoItem.isAdaCompleted
                notifyItemChanged(adapterPosition) // Or notifyItemChanged(position) if adapterPosition is -1
            }
        }
    }
}

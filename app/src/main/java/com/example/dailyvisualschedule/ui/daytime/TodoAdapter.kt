package com.example.dailyvisualschedule.ui.daytime

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyvisualschedule.databinding.ListItemTodoBinding
import com.example.dailyvisualschedule.ui.common.TaskStarViewModel // Import the interface

class TodoAdapter(
    private val todoItems: MutableList<TodoItem>,
    private val viewModel: TaskStarViewModel 
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

    fun submitList(newTodoItems: List<TodoItem>) {
        todoItems.clear()
        todoItems.addAll(newTodoItems)
        notifyDataSetChanged() // Consider using DiffUtil for better performance
    }

    inner class TodoViewHolder(private val binding: ListItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(todoItem: TodoItem) {
            binding.imageViewTaskIcon.setImageResource(todoItem.imageResId)
            // Assuming you might add a TextView for the name later:
            // binding.textViewTaskName.text = todoItem.name 

            // Ellie's star
            if (todoItem.isEllieCompleted) {
                binding.buttonStarEllie.setImageResource(android.R.drawable.btn_star_big_on)
            } else {
                binding.buttonStarEllie.setImageResource(android.R.drawable.btn_star_big_off)
            }
            binding.buttonStarEllie.setOnClickListener {
                // The actual state is toggled in the ViewModel after DB update
                // So we pass the NEW intended state.
                val newEllieCompletedState = !todoItem.isEllieCompleted
                viewModel.ellieStarStateChanged(todoItem.id, newEllieCompletedState)
                // UI will update once LiveData from ViewModel emits new list from DB
            }

            // Ada's star
            if (todoItem.isAdaCompleted) {
                binding.buttonStarAda.setImageResource(android.R.drawable.btn_star_big_on)
            } else {
                binding.buttonStarAda.setImageResource(android.R.drawable.btn_star_big_off)
            }
            binding.buttonStarAda.setOnClickListener {
                val newAdaCompletedState = !todoItem.isAdaCompleted
                viewModel.adaStarStateChanged(todoItem.id, newAdaCompletedState)
            }
        }
    }
}

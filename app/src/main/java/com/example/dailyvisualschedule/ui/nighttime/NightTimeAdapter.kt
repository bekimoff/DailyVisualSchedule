package com.example.dailyvisualschedule.ui.nighttime

// Removed: import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyvisualschedule.databinding.ListItemTodoBinding // Assuming reuse from daytime
import com.example.dailyvisualschedule.ui.common.TaskStarViewModel
import com.example.dailyvisualschedule.ui.daytime.TodoItem // Assuming reuse from daytime
// Removed: import java.util.Collections

// Removed NightTimeItemMoveCallbackListener interface

class NightTimeAdapter(
    private val todoItems: MutableList<TodoItem>, // Using TodoItem from daytime for now
    private val viewModel: TaskStarViewModel // Assuming NightTimeViewModel implements this
    // Removed itemMoveListener parameter
) : RecyclerView.Adapter<NightTimeAdapter.NightTimeTodoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NightTimeTodoViewHolder {
        // Assuming reuse of ListItemTodoBinding
        val binding = ListItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NightTimeTodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NightTimeTodoViewHolder, position: Int) {
        val todoItem = todoItems[position]
        holder.bind(todoItem)
    }

    override fun getItemCount(): Int = todoItems.size

    fun submitList(newTodoItems: List<TodoItem>) {
        todoItems.clear()
        todoItems.addAll(newTodoItems)
        notifyDataSetChanged() // Consider using DiffUtil for better performance
    }

    // Removed onItemMove method

    // Removed getCurrentItems method

    inner class NightTimeTodoViewHolder(val binding: ListItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(todoItem: TodoItem) {
            binding.imageViewTaskIcon.setImageResource(todoItem.imageResId)
            // binding.textViewTaskName.text = todoItem.name // If you have a name TextView

            if (todoItem.isEllieCompleted) {
                binding.buttonStarEllie.setImageResource(android.R.drawable.btn_star_big_on)
            } else {
                binding.buttonStarEllie.setImageResource(android.R.drawable.btn_star_big_off)
            }
            binding.buttonStarEllie.setOnClickListener {
                val newEllieCompletedState = !todoItem.isEllieCompleted
                viewModel.ellieStarStateChanged(todoItem.id, newEllieCompletedState)
            }

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

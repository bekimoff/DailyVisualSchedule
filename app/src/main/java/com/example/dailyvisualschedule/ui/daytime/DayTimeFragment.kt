package com.example.dailyvisualschedule.ui.daytime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dailyvisualschedule.databinding.FragmentDayTimeBinding
import com.example.dailyvisualschedule.R // Make sure this R is your project's R

class DayTimeFragment : Fragment() {

    private var _binding: FragmentDayTimeBinding? = null
    private val binding get() = _binding!!

    private lateinit var todoAdapter: TodoAdapter
    private val todoList = mutableListOf<TodoItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDayTimeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Sample Data - Ensure these drawables exist in your res/drawable folder!
        todoList.clear() // Clear any previous items
        todoList.add(TodoItem(R.drawable.ic_toilet))          // Task: Use toilet
        todoList.add(TodoItem(R.drawable.ic_toothbrush))      // Task: Brush teeth
        todoList.add(TodoItem(R.drawable.ic_clothes)) // Placeholder for "Eat breakfast"
        todoList.add(TodoItem(R.drawable.ic_shoes))           // Task: Put on shoes
        todoList.add(TodoItem(R.drawable.ic_backpack))        // Task: Take backpack

        todoAdapter = TodoAdapter(todoList)

        binding.recyclerViewTodo.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = todoAdapter
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

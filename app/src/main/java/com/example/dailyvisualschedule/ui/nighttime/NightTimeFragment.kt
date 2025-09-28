package com.example.dailyvisualschedule.ui.nighttime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dailyvisualschedule.databinding.FragmentNightTimeBinding
import com.example.dailyvisualschedule.ui.daytime.TodoAdapter // Reusing from daytime package
import com.example.dailyvisualschedule.ui.daytime.TodoItem    // Reusing from daytime package
import com.example.dailyvisualschedule.R // Make sure this R is your project's R

class NightTimeFragment : Fragment() {

    private var _binding: FragmentNightTimeBinding? = null
    private val binding get() = _binding!!

    private lateinit var todoAdapter: TodoAdapter
    private val todoList = mutableListOf<TodoItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNightTimeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Sample Nighttime Data - Ensure these drawables exist in your res/drawable folder!
        todoList.clear() // Clear any previous items
        todoList.add(TodoItem(R.drawable.ic_toilet))          // Task: Use toilet
        todoList.add(TodoItem(R.drawable.ic_toothbrush))      // Task: Brush teeth
        todoList.add(TodoItem(R.drawable.ic_broom))           // Task: Sweep floor (example)
        todoList.add(TodoItem(R.drawable.ic_book))            // Task: Read book
        todoList.add(TodoItem(R.drawable.ic_pyjamas)) // Placeholder for "Go to bed"

        todoAdapter = TodoAdapter(todoList)

        binding.recyclerViewTodoNight.apply {
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

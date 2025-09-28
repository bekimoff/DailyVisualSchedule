package com.example.dailyvisualschedule.ui.daytime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dailyvisualschedule.VisualScheduleApplication // Import custom Application class
import com.example.dailyvisualschedule.databinding.FragmentDayTimeBinding
import com.example.dailyvisualschedule.ui.ViewModelFactory // Import factory

class DayTimeFragment : Fragment() {

    private var _binding: FragmentDayTimeBinding? = null
    private val binding get() = _binding!!

    private lateinit var todoAdapter: TodoAdapter
    private lateinit var dayTimeViewModel: DayTimeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDayTimeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize ViewModel using the factory
        val application = requireActivity().application as VisualScheduleApplication
        val factory = ViewModelFactory(application.taskDataRepository)
        dayTimeViewModel = ViewModelProvider(this, factory).get(DayTimeViewModel::class.java)

        // Initialize Adapter with the ViewModel
        todoAdapter = TodoAdapter(mutableListOf(), dayTimeViewModel)

        binding.recyclerViewTodo.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = todoAdapter
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe LiveData from ViewModel
        dayTimeViewModel.todoItems.observe(viewLifecycleOwner) { items ->
            items?.let {
                todoAdapter.submitList(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerViewTodo.adapter = null 
        _binding = null
    }
}

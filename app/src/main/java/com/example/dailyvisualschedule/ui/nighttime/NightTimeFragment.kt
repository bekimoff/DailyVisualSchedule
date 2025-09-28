package com.example.dailyvisualschedule.ui.nighttime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dailyvisualschedule.VisualScheduleApplication // Import custom Application class
import com.example.dailyvisualschedule.databinding.FragmentNightTimeBinding
import com.example.dailyvisualschedule.ui.ViewModelFactory // Import factory
import com.example.dailyvisualschedule.ui.daytime.TodoAdapter // Reusing from daytime package

class NightTimeFragment : Fragment() {

    private var _binding: FragmentNightTimeBinding? = null
    private val binding get() = _binding!!

    private lateinit var todoAdapter: TodoAdapter
    private lateinit var nightTimeViewModel: NightTimeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNightTimeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize ViewModel using the factory
        val application = requireActivity().application as VisualScheduleApplication
        val factory = ViewModelFactory(application.taskDataRepository)
        nightTimeViewModel = ViewModelProvider(this, factory).get(NightTimeViewModel::class.java)

        // Initialize Adapter with the ViewModel
        todoAdapter = TodoAdapter(mutableListOf(), nightTimeViewModel)

        binding.recyclerViewTodoNight.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = todoAdapter
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe LiveData from ViewModel
        nightTimeViewModel.todoItems.observe(viewLifecycleOwner) { items ->
            items?.let {
                todoAdapter.submitList(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerViewTodoNight.adapter = null 
        _binding = null
    }
}

package com.example.dailyvisualschedule.ui.daytime

// Removed: import android.graphics.Color
import android.os.Bundle
// Removed: import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
// Removed: import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
// Removed: import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
// Removed: import androidx.recyclerview.widget.RecyclerView
// Removed: import com.example.dailyvisualschedule.R
import com.example.dailyvisualschedule.VisualScheduleApplication
import com.example.dailyvisualschedule.databinding.FragmentDayTimeBinding
import com.example.dailyvisualschedule.ui.ViewModelFactory

// Removed ItemMoveCallbackListener implementation
class DayTimeFragment : Fragment() {

    private var _binding: FragmentDayTimeBinding? = null
    private val binding get() = _binding!!

    private lateinit var todoAdapter: TodoAdapter
    private lateinit var dayTimeViewModel: DayTimeViewModel
    // Removed: private var itemTouchHelper: ItemTouchHelper? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDayTimeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val application = requireActivity().application as VisualScheduleApplication
        val factory = ViewModelFactory(application.taskDataRepository)
        dayTimeViewModel = ViewModelProvider(this, factory).get(DayTimeViewModel::class.java)

        // Reverted TodoAdapter instantiation
        todoAdapter = TodoAdapter(mutableListOf(), dayTimeViewModel)

        binding.recyclerViewTodo.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = todoAdapter
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dayTimeViewModel.todoItems.observe(viewLifecycleOwner) { items ->
            items?.let {
                todoAdapter.submitList(it)
            }
        }

        // Removed ItemTouchHelper setup
    }

    // Removed ItemMoveCallbackListener methods (onRowMovedAndClear, onRowSelected, onRowClear)

    override fun onDestroyView() {
        super.onDestroyView()
        // Removed: itemTouchHelper?.attachToRecyclerView(null)
        binding.recyclerViewTodo.adapter = null 
        _binding = null
    }
}

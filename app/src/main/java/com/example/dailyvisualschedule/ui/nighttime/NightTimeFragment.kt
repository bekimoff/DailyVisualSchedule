package com.example.dailyvisualschedule.ui.nighttime

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
import com.example.dailyvisualschedule.VisualScheduleApplication
import com.example.dailyvisualschedule.databinding.FragmentNightTimeBinding
import com.example.dailyvisualschedule.ui.ViewModelFactory

// Removed NightTimeItemMoveCallbackListener implementation
class NightTimeFragment : Fragment() {

    private var _binding: FragmentNightTimeBinding? = null
    private val binding get() = _binding!!

    private lateinit var nightTimeAdapter: NightTimeAdapter
    private lateinit var nightTimeViewModel: NightTimeViewModel
    // Removed: private var itemTouchHelper: ItemTouchHelper? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNightTimeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val application = requireActivity().application as VisualScheduleApplication
        val factory = ViewModelFactory(application.taskDataRepository)
        nightTimeViewModel = ViewModelProvider(this, factory).get(NightTimeViewModel::class.java)

        // Reverted NightTimeAdapter instantiation
        nightTimeAdapter = NightTimeAdapter(mutableListOf(), nightTimeViewModel)

        binding.recyclerViewTodoNight.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = nightTimeAdapter
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nightTimeViewModel.todoItems.observe(viewLifecycleOwner) { items ->
            items?.let {
                nightTimeAdapter.submitList(it)
            }
        }

        // Removed ItemTouchHelper setup
    }

    // Removed NightTimeItemMoveCallbackListener methods (onRowMovedAndClear, onRowSelected, onRowClear)

    override fun onDestroyView() {
        super.onDestroyView()
        // Removed: itemTouchHelper?.attachToRecyclerView(null)
        binding.recyclerViewTodoNight.adapter = null 
        _binding = null
    }
}

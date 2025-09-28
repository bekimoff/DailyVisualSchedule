package com.example.dailyvisualschedule.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider // Keep for basic structure
import com.example.dailyvisualschedule.databinding.FragmentDashboardBinding
// Explicitly import R class, just in case, though usually not needed with view binding alone
import com.example.dailyvisualschedule.R

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Basic ViewModel instantiation (can be removed if truly not needed for a minimal test)
        val dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        // Line 20 is now below this comment, containing no problematic references

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

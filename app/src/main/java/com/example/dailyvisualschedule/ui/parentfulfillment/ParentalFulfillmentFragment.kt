package com.example.dailyvisualschedule.ui.parentfulfillment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast // Added for Toast messages
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dailyvisualschedule.VisualScheduleApplication
import com.example.dailyvisualschedule.databinding.FragmentParentalFulfillmentBinding
import com.example.dailyvisualschedule.ui.ViewModelFactory

class ParentalFulfillmentFragment : Fragment() {

    private var _binding: FragmentParentalFulfillmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ParentalFulfillmentViewModel
    private lateinit var unfulfilledRewardsAdapter: UnfulfilledRewardsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentParentalFulfillmentBinding.inflate(inflater, container, false)

        val application = requireActivity().application as VisualScheduleApplication
        val factory = ViewModelFactory(application.taskDataRepository)
        viewModel = ViewModelProvider(this, factory).get(ParentalFulfillmentViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
        setupManualStarSetters()
    }

    private fun setupRecyclerView() {
        unfulfilledRewardsAdapter = UnfulfilledRewardsAdapter { rewardEntry ->
            viewModel.markRewardAsFulfilled(rewardEntry)
        }

        binding.recyclerViewUnfulfilledRewards.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = unfulfilledRewardsAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.unfulfilledRewards.observe(viewLifecycleOwner) { rewards ->
            unfulfilledRewardsAdapter.submitList(rewards)
            if (rewards.isNullOrEmpty()) {
                binding.recyclerViewUnfulfilledRewards.visibility = View.GONE
                binding.textViewNoUnfulfilledRewards.visibility = View.VISIBLE
            } else {
                binding.recyclerViewUnfulfilledRewards.visibility = View.VISIBLE
                binding.textViewNoUnfulfilledRewards.visibility = View.GONE
            }
        }

        viewModel.ellieStarCount.observe(viewLifecycleOwner) { count ->
            binding.textViewEllieCurrentStarsValue.text = count.toString()
        }

        viewModel.adaStarCount.observe(viewLifecycleOwner) { count ->
            binding.textViewAdaCurrentStarsValue.text = count.toString()
        }
    }

    private fun setupManualStarSetters() {
        binding.buttonEllieSetStars.setOnClickListener {
            val newStarTotalString = binding.editTextEllieSetStars.text.toString()
            try {
                val newStarTotal = newStarTotalString.toInt()
                if (newStarTotal >= 0) {
                    viewModel.setStarCountForChild("ellie", newStarTotal)
                    binding.editTextEllieSetStars.text.clear()
                    Toast.makeText(context, "Ellie's stars updated to $newStarTotal", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Star total cannot be negative.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: NumberFormatException) {
                Toast.makeText(context, "Invalid number for Ellie's stars.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonAdaSetStars.setOnClickListener {
            val newStarTotalString = binding.editTextAdaSetStars.text.toString()
            try {
                val newStarTotal = newStarTotalString.toInt()
                if (newStarTotal >= 0) {
                    viewModel.setStarCountForChild("ada", newStarTotal)
                    binding.editTextAdaSetStars.text.clear()
                    Toast.makeText(context, "Ada's stars updated to $newStarTotal", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Star total cannot be negative.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: NumberFormatException) {
                Toast.makeText(context, "Invalid number for Ada's stars.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerViewUnfulfilledRewards.adapter = null 
        _binding = null
    }
}

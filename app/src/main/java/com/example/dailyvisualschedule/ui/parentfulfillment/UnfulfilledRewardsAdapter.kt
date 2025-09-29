package com.example.dailyvisualschedule.ui.parentfulfillment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyvisualschedule.data.RedeemedRewardEntry
import com.example.dailyvisualschedule.databinding.ListItemUnfulfilledRewardBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UnfulfilledRewardsAdapter(
    private val onRewardFulfilled: (RedeemedRewardEntry) -> Unit
) : ListAdapter<RedeemedRewardEntry, UnfulfilledRewardsAdapter.ViewHolder>(RedeemedRewardDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemUnfulfilledRewardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onRewardFulfilled)
    }

    class ViewHolder(private val binding: ListItemUnfulfilledRewardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val dateFormatter = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())

        fun bind(rewardEntry: RedeemedRewardEntry, onRewardFulfilled: (RedeemedRewardEntry) -> Unit) {
            binding.textViewChildName.text = rewardEntry.childName
            binding.textViewRewardItemDescription.text =
                "${rewardEntry.rewardDescription} (${rewardEntry.rewardPoints} stars)"
            binding.textViewRedemptionDate.text =
                "Redeemed: ${dateFormatter.format(Date(rewardEntry.redemptionTimestamp))}"

            // Set the checkbox state WITHOUT triggering the listener initially
            binding.checkBoxFulfilled.setOnCheckedChangeListener(null)
            binding.checkBoxFulfilled.isChecked = rewardEntry.isFulfilled

            // Set the listener for user interactions
            binding.checkBoxFulfilled.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) { // Only trigger if moving to fulfilled
                    onRewardFulfilled(rewardEntry)
                }
                // If you want to allow un-fulfilling, you might adjust logic or the listener
            }
        }
    }
}

class RedeemedRewardDiffCallback : DiffUtil.ItemCallback<RedeemedRewardEntry>() {
    override fun areItemsTheSame(oldItem: RedeemedRewardEntry, newItem: RedeemedRewardEntry): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RedeemedRewardEntry, newItem: RedeemedRewardEntry): Boolean {
        return oldItem == newItem
    }
}

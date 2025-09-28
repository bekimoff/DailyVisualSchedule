package com.example.dailyvisualschedule.ui.rewards

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.dailyvisualschedule.databinding.FragmentRewardsBinding

class RewardsFragment : Fragment() {

    private var _binding: FragmentRewardsBinding? = null
    private val binding get() = _binding!!

    private val milestoneValues = arrayOf(10, 50, 100, 200)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRewardsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Sample progress values
        val progress1 = 75 // Example: 75 points for reward 1
        val progress2 = 120 // Example: 120 points for reward 2
        val maxProgress = 200

        binding.progressBar1.max = maxProgress
        binding.progressBar1.progress = progress1
        binding.textViewProgress1Value.text = "$progress1/$maxProgress"
        updateMilestoneStars(progress1, listOf(
            binding.imageViewStar110,
            binding.imageViewStar150,
            binding.imageViewStar1100,
            binding.imageViewStar1200
        ))

        binding.progressBar2.max = maxProgress
        binding.progressBar2.progress = progress2
        binding.textViewProgress2Value.text = "$progress2/$maxProgress"
        updateMilestoneStars(progress2, listOf(
            binding.imageViewStar210,
            binding.imageViewStar250,
            binding.imageViewStar2100,
            binding.imageViewStar2200
        ))

        binding.buttonRedeem1.setOnClickListener {
            showMilestoneRedemptionDialog("Reward 1", binding.progressBar1.progress, maxProgress)
        }

        binding.buttonRedeem2.setOnClickListener {
            showMilestoneRedemptionDialog("Reward 2", binding.progressBar2.progress, maxProgress)
        }

        return root
    }

    private fun updateMilestoneStars(currentProgress: Int, starImageViews: List<ImageView>) {
        starImageViews.forEachIndexed { index, imageView ->
            if (currentProgress >= milestoneValues[index]) {
                imageView.visibility = View.VISIBLE
            } else {
                imageView.visibility = View.GONE
            }
        }
    }

    private fun showMilestoneRedemptionDialog(rewardName: String, currentProgress: Int, maxProgress: Int) {
        val milestones = arrayOf("10 points", "50 points", "100 points", "200 points")
        // milestoneValues is already a class member

        AlertDialog.Builder(requireContext())
            .setTitle("Redeem $rewardName")
            .setItems(milestones) { dialog, which ->
                val selectedMilestoneValue = milestoneValues[which]
                if (currentProgress >= selectedMilestoneValue) {
                    Toast.makeText(context, "Redeemed $selectedMilestoneValue points for $rewardName!", Toast.LENGTH_SHORT).show()
                    // TODO: Add logic to update progress, reflect redemption, and then update stars again
                    // For example, if progress changes for progressBar1:
                    // binding.progressBar1.progress = newProgress1
                    // binding.textViewProgress1Value.text = "$newProgress1/$maxProgress"
                    // updateMilestoneStars(newProgress1, listOf(binding.imageViewStar110, binding.imageViewStar150, binding.imageViewStar1100, binding.imageViewStar1200))
                } else {
                    Toast.makeText(context, "Not enough points to redeem $selectedMilestoneValue for $rewardName.", Toast.LENGTH_LONG).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package com.example.dailyvisualschedule.ui.rewards

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView // Import TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.dailyvisualschedule.R
import com.example.dailyvisualschedule.VisualScheduleApplication
import com.example.dailyvisualschedule.databinding.FragmentRewardsBinding
import com.example.dailyvisualschedule.ui.ViewModelFactory

// Data class to hold milestone reward information
data class MilestoneReward(val points: Int, val description: String)

class RewardsFragment : Fragment() {

    private var _binding: FragmentRewardsBinding? = null
    private val binding get() = _binding!!
    private lateinit var rewardsViewModel: RewardsViewModel

    private val milestoneRewards = listOf(
        MilestoneReward(10, "3 stickers 🌟🌟🌟"),
        MilestoneReward(50, "Lollipop 🍭"),
        MilestoneReward(100, "Sticker book or new book 📚"),
        MilestoneReward(150, "Ice Cream 🍦"),
        MilestoneReward(200, "New Stuffie 🧸")
    ).sortedBy { it.points }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val application = requireActivity().application as VisualScheduleApplication
        val factory = ViewModelFactory(application.taskDataRepository)
        rewardsViewModel = ViewModelProvider(this, factory).get(RewardsViewModel::class.java)
        
        _binding = FragmentRewardsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setHasOptionsMenu(true) // Indicate that this fragment has an options menu

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val maxProgress = 200

        rewardsViewModel.ellieStars.observe(viewLifecycleOwner) { count ->
            binding.progressBar1.progress = count
            binding.textViewProgress1Value.text = "$count/$maxProgress"
            updateMilestoneVisibility(count, "Ellie") 
        }

        rewardsViewModel.adaStars.observe(viewLifecycleOwner) { count ->
            binding.progressBar2.progress = count
            binding.textViewProgress2Value.text = "$count/$maxProgress"
            updateMilestoneVisibility(count, "Ada")
        }

        binding.buttonRedeem1.setOnClickListener {
            showRedeemDialog("Ellie", rewardsViewModel.ellieStars.value ?: 0)
        }

        binding.buttonRedeem2.setOnClickListener {
            showRedeemDialog("Ada", rewardsViewModel.adaStars.value ?: 0)
        }
        
        populateRewardListTextViews()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.rewards_options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_parental_controls -> {
                showPasswordDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showPasswordDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Enter Password")

        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            val password = input.text.toString()
            // For now, using a hardcoded password. 
            // Consider a more secure way to store/manage this if needed.
            if (password == "1234") { 
                Toast.makeText(context, "Password Correct! Navigating to Parental Controls...", Toast.LENGTH_SHORT).show()
                // TODO: Navigate to the Parental Fulfillment Fragment here
            } else {
                Toast.makeText(context, "Incorrect Password", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    private fun populateRewardListTextViews() {
        val rewardTextViews: Map<Int, TextView> = mapOf(
            10 to binding.textViewRewardDescription10,
            50 to binding.textViewRewardDescription50,
            100 to binding.textViewRewardDescription100,
            150 to binding.textViewRewardDescription150,
            200 to binding.textViewRewardDescription200
        )

        milestoneRewards.forEach { reward ->
            rewardTextViews[reward.points]?.text = "${reward.points} stars: ${reward.description}"
        }
    }

    private fun showRedeemDialog(userName: String, currentStars: Int) {
        val affordableRewards = milestoneRewards.filter { it.points <= currentStars }
        if (affordableRewards.isEmpty()) {
            Toast.makeText(context, "Not enough stars for any reward.", Toast.LENGTH_SHORT).show()
            return
        }
        val rewardOptionsText = affordableRewards.map { "${it.points} stars: ${it.description}" }.toTypedArray()
        AlertDialog.Builder(requireContext())
            .setTitle("Redeem Reward for $userName")
            .setItems(rewardOptionsText) { dialog, which ->
                val selectedReward = affordableRewards[which]
                showConfirmationDialog(userName, selectedReward)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun showConfirmationDialog(userName: String, rewardToRedeem: MilestoneReward) {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirm Redemption")
            .setMessage("Redeem \"${rewardToRedeem.description}\" for $userName? This will deduct ${rewardToRedeem.points} stars.")
            .setPositiveButton("Confirm") { dialog, _ ->
                val success = rewardsViewModel.redeemStars(userName, rewardToRedeem.points)
                if (success) {
                    Toast.makeText(context, "\"${rewardToRedeem.description}\" redeemed for $userName!", Toast.LENGTH_LONG).show()
                    sendRewardEmail(userName, rewardToRedeem)
                    // TODO: Add call to repository to store this redeemed reward instance for parent fulfillment list
                } else {
                    Toast.makeText(context, "Redemption failed unexpectedly.", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun sendRewardEmail(userName: String, redeemedReward: MilestoneReward) {
        val recipientEmails = arrayOf("bekimoff@gmail.com", "billnreed@gmail.com")
        val subject = "Reward Claimed: $userName - Daily Visual Schedule"
        val body = "$userName has redeemed the reward: \"${redeemedReward.description}\" for ${redeemedReward.points} stars."
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, recipientEmails)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "No email app found to send notification.", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateMilestoneVisibility(count: Int, userName: String) {
        if (userName == "Ellie") {
            binding.imageViewStar110.visibility = if (count >= 10) View.VISIBLE else View.GONE
            binding.imageViewStar150.visibility = if (count >= 50) View.VISIBLE else View.GONE
            binding.imageViewStar1100.visibility = if (count >= 100) View.VISIBLE else View.GONE
            binding.imageViewStar1200.visibility = if (count >= 200) View.VISIBLE else View.GONE
            binding.buttonRedeem1.isEnabled = milestoneRewards.any { count >= it.points } 
        } else if (userName == "Ada") {
            binding.imageViewStar210.visibility = if (count >= 10) View.VISIBLE else View.GONE
            binding.imageViewStar250.visibility = if (count >= 50) View.VISIBLE else View.GONE
            binding.imageViewStar2100.visibility = if (count >= 100) View.VISIBLE else View.GONE
            binding.imageViewStar2200.visibility = if (count >= 200) View.VISIBLE else View.GONE
            binding.buttonRedeem2.isEnabled = milestoneRewards.any { count >= it.points }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

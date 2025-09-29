package com.example.dailyvisualschedule.ui.parentfulfillment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.dailyvisualschedule.data.RedeemedRewardEntry
import com.example.dailyvisualschedule.data.TaskDataRepository
import kotlinx.coroutines.flow.map // Required for mapping StateFlow if needed, though asLiveData handles direct conversion
import kotlinx.coroutines.launch

class ParentalFulfillmentViewModel(private val repository: TaskDataRepository) : ViewModel() {

    // LiveData to hold the list of unfulfilled rewards
    val unfulfilledRewards: LiveData<List<RedeemedRewardEntry>> = repository.getUnfulfilledRewards().asLiveData()

    // LiveData for star counts
    val ellieStarCount: LiveData<Int> = repository.ellieStarCount.asLiveData()
    val adaStarCount: LiveData<Int> = repository.adaStarCount.asLiveData()

    /**
     * Marks a redeemed reward as fulfilled.
     */
    fun markRewardAsFulfilled(rewardEntry: RedeemedRewardEntry) {
        viewModelScope.launch {
            val updatedEntry = rewardEntry.copy(isFulfilled = true)
            repository.updateRedeemedRewardEntry(updatedEntry)
            // The LiveData for unfulfilledRewards will automatically update the UI
        }
    }

    /**
     * Manually sets the star count for a specific child.
     */
    fun setStarCountForChild(childName: String, newTotal: Int) {
        // The repository's StateFlows (_ellieStarCount, _adaStarCount) will be updated,
        // which will in turn update the ellieStarCount and adaStarCount LiveData.
        repository.updateStarCountManual(childName, newTotal)
    }

    /**
     * If you want to delete the reward entry after fulfilling (optional)
     */
    fun deleteFulfilledReward(rewardEntry: RedeemedRewardEntry) {
        viewModelScope.launch {
            // repository.deleteRedeemedRewardById(rewardEntry.id) // You'd need to add this method to DAO and Repository
        }
    }
}

package com.example.dailyvisualschedule.ui.rewards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.dailyvisualschedule.data.TaskDataRepository
import kotlinx.coroutines.launch

class RewardsViewModel(private val repository: TaskDataRepository) : ViewModel() {

    val ellieStars: LiveData<Int> = repository.ellieStarCount.asLiveData()
    val adaStars: LiveData<Int> = repository.adaStarCount.asLiveData()

    fun addStarForEllie() {
        repository.incrementStarCount("Ellie")
    }

    fun addStarForAda() {
        repository.incrementStarCount("Ada")
    }

    fun resetAllStarCounts() {
        repository.resetStarCount("Ellie")
        repository.resetStarCount("Ada")
    }
    
    fun removeStarForEllie() {
        repository.decrementStarCount("Ellie")
    }

    fun removeStarForAda() {
        repository.decrementStarCount("Ada")
    }

    /**
     * Attempts to deduct stars for the given user.
     * Returns true if successful, false otherwise (e.g., not enough stars).
     */
    fun redeemStars(userName: String, starsToDeduct: Int): Boolean {
        return repository.deductStars(userName, starsToDeduct)
    }

    /**
     * Records the instance of a reward being redeemed.
     */
    fun recordRewardRedemption(childName: String, reward: MilestoneReward) {
        viewModelScope.launch {
            repository.addRedeemedReward(childName, reward)
        }
    }
}

package com.example.dailyvisualschedule.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dailyvisualschedule.data.TaskDataRepository
import com.example.dailyvisualschedule.ui.daytime.DayTimeViewModel
import com.example.dailyvisualschedule.ui.nighttime.NightTimeViewModel
import com.example.dailyvisualschedule.ui.parentfulfillment.ParentalFulfillmentViewModel // Added import
import com.example.dailyvisualschedule.ui.rewards.RewardsViewModel

/**
 * ViewModel provider factory to instantiate ViewModels with a TaskDataRepository.
 */
class ViewModelFactory(private val repository: TaskDataRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DayTimeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DayTimeViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(NightTimeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NightTimeViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(RewardsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RewardsViewModel(repository) as T
        }
        // Added case for ParentalFulfillmentViewModel
        if (modelClass.isAssignableFrom(ParentalFulfillmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ParentalFulfillmentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}

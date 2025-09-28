package com.example.dailyvisualschedule.ui.common

/**
 * Interface for ViewModels that manage tasks with Ellie and Ada stars 
 * and need to update their counts via the StarCounterRepository.
 */
interface TaskStarViewModel {
    fun ellieStarStateChanged(itemId: Int, isNowCompleted: Boolean)
    fun adaStarStateChanged(itemId: Int, isNowCompleted: Boolean)
}

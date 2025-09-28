package com.example.dailyvisualschedule.ui.daytime

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.dailyvisualschedule.data.PersistentTodoItem
import com.example.dailyvisualschedule.data.TaskDataRepository
import com.example.dailyvisualschedule.ui.common.TaskStarViewModel
import kotlinx.coroutines.flow.map // Import for Flow's map operator
import kotlinx.coroutines.launch

class DayTimeViewModel(private val repository: TaskDataRepository) : ViewModel(), TaskStarViewModel {

    val todoItems: LiveData<List<TodoItem>> = repository.getTasksByScheduleType("day")
        .map { persistentItems -> // Perform mapping within the Flow
            persistentItems.map { persistentItem ->
                TodoItem(
                    id = persistentItem.id,
                    name = persistentItem.taskName,
                    imageResId = persistentItem.imageResId,
                    isEllieCompleted = persistentItem.isEllieCompleted,
                    isAdaCompleted = persistentItem.isAdaCompleted
                )
            }
        }
        .asLiveData() // Convert the mapped Flow to LiveData

    override fun ellieStarStateChanged(itemId: Int, isNowCompleted: Boolean) {
        viewModelScope.launch {
            val currentUiItem = todoItems.value?.find { it.id == itemId }
            currentUiItem?.let {
                val persistentItemToUpdate = PersistentTodoItem(
                    id = it.id,
                    taskName = it.name, 
                    imageResId = it.imageResId, 
                    isEllieCompleted = isNowCompleted, 
                    isAdaCompleted = it.isAdaCompleted, 
                    scheduleType = "day"
                )
                repository.updateTask(persistentItemToUpdate)

                if (isNowCompleted) {
                    repository.incrementStarCount("Ellie")
                } else {
                    repository.decrementStarCount("Ellie")
                }
            } ?: run { // Changed to run
                println("DayTimeViewModel: Ellie star changed for unknown item ID: $itemId")
            }
        }
    }

    override fun adaStarStateChanged(itemId: Int, isNowCompleted: Boolean) {
        viewModelScope.launch {
            val currentUiItem = todoItems.value?.find { it.id == itemId }
            currentUiItem?.let {
                val persistentItemToUpdate = PersistentTodoItem(
                    id = it.id,
                    taskName = it.name,
                    imageResId = it.imageResId,
                    isEllieCompleted = it.isEllieCompleted, 
                    isAdaCompleted = isNowCompleted, 
                    scheduleType = "day"
                )
                repository.updateTask(persistentItemToUpdate)

                if (isNowCompleted) {
                    repository.incrementStarCount("Ada")
                } else {
                    repository.decrementStarCount("Ada")
                }
            } ?: run { // Changed to run
                println("DayTimeViewModel: Ada star changed for unknown item ID: $itemId")
            }
        }
    }
}

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
                    // Removed: orderIndex = persistentItem.orderIndex
                )
            }
        }
        .asLiveData() // Convert the mapped Flow to LiveData

    // Removed updateTaskOrder function

    override fun ellieStarStateChanged(itemId: Int, isNowCompleted: Boolean) {
        viewModelScope.launch {
            val uiItem = todoItems.value?.find { it.id == itemId }
            if (uiItem != null) {
                val persistentItemToUpdate = PersistentTodoItem(
                    id = uiItem.id,
                    taskName = uiItem.name, 
                    imageResId = uiItem.imageResId, 
                    isEllieCompleted = isNowCompleted, 
                    isAdaCompleted = uiItem.isAdaCompleted, 
                    scheduleType = "day"
                    // Removed: orderIndex = uiItem.orderIndex
                )
                repository.updateTask(persistentItemToUpdate)

                if (isNowCompleted) {
                    repository.incrementStarCount("Ellie")
                } else {
                    repository.decrementStarCount("Ellie")
                }
            } else {
                println("DayTimeViewModel: Ellie star changed for unknown item ID: $itemId")
            }
        }
    }

    override fun adaStarStateChanged(itemId: Int, isNowCompleted: Boolean) {
        viewModelScope.launch {
            val uiItem = todoItems.value?.find { it.id == itemId }
            if (uiItem != null) {
                val persistentItemToUpdate = PersistentTodoItem(
                    id = uiItem.id,
                    taskName = uiItem.name,
                    imageResId = uiItem.imageResId,
                    isEllieCompleted = uiItem.isEllieCompleted, 
                    isAdaCompleted = isNowCompleted, 
                    scheduleType = "day"
                    // Removed: orderIndex = uiItem.orderIndex
                )
                repository.updateTask(persistentItemToUpdate)

                if (isNowCompleted) {
                    repository.incrementStarCount("Ada")
                } else {
                    repository.decrementStarCount("Ada")
                }
            } else {
                println("DayTimeViewModel: Ada star changed for unknown item ID: $itemId")
            }
        }
    }
}

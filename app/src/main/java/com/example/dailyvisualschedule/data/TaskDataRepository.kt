package com.example.dailyvisualschedule.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TaskDataRepository(private val todoItemDao: TodoItemDao, context: Context) {

    private val prefsName = "StarCountsPrefs" // Can use the same prefs file or a new one
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)

    companion object {
        private const val KEY_ELLIE_STARS = "ellie_stars"
        private const val KEY_ADA_STARS = "ada_stars"
        private const val KEY_LAST_RESET_DATE = "last_reset_date" // New key
    }

    // --- Star Counts with StateFlow ---
    private val _ellieStarCount = MutableStateFlow(sharedPreferences.getInt(KEY_ELLIE_STARS, 0))
    val ellieStarCount: StateFlow<Int> = _ellieStarCount.asStateFlow()

    private val _adaStarCount = MutableStateFlow(sharedPreferences.getInt(KEY_ADA_STARS, 0))
    val adaStarCount: StateFlow<Int> = _adaStarCount.asStateFlow()

    fun getStarCount(userName: String): Int {
        return when (userName.lowercase()) {
            "ellie" -> _ellieStarCount.value
            "ada" -> _adaStarCount.value
            else -> 0
        }
    }

    private fun setStarCount(userName: String, count: Int) {
        val safeCount = if (count < 0) 0 else count 
        when (userName.lowercase()) {
            "ellie" -> {
                sharedPreferences.edit().putInt(KEY_ELLIE_STARS, safeCount).apply()
                _ellieStarCount.value = safeCount
            }
            "ada" -> {
                sharedPreferences.edit().putInt(KEY_ADA_STARS, safeCount).apply()
                _adaStarCount.value = safeCount
            }
        }
    }

    fun incrementStarCount(userName: String): Int {
        val currentCount = getStarCount(userName)
        val newCount = currentCount + 1
        setStarCount(userName, newCount)
        return newCount
    }

    fun decrementStarCount(userName: String): Int {
        val currentCount = getStarCount(userName)
        if (currentCount > 0) {
            val newCount = currentCount - 1
            setStarCount(userName, newCount)
            return newCount
        }
        return currentCount
    }
    
    fun deductStars(userName: String, amountToDeduct: Int): Boolean {
        val currentCount = getStarCount(userName)
        if (currentCount >= amountToDeduct) {
            val newCount = currentCount - amountToDeduct
            setStarCount(userName, newCount)
            return true 
        } 
        return false 
    }

    fun resetStarCount(userName: String) {
        setStarCount(userName, 0)
    }

    // --- Last Reset Date Management ---
    fun getLastResetDate(): String {
        return sharedPreferences.getString(KEY_LAST_RESET_DATE, "") ?: "" // Default to empty string
    }

    fun setLastResetDate(date: String) {
        sharedPreferences.edit().putString(KEY_LAST_RESET_DATE, date).apply()
    }

    // --- Room Database Operations for Task Items ---
    fun getTasksByScheduleType(scheduleType: String): Flow<List<PersistentTodoItem>> {
        return todoItemDao.getTasksByScheduleType(scheduleType)
    }

    suspend fun updateTask(item: PersistentTodoItem) {
        todoItemDao.update(item)
    }
    
    suspend fun insertTask(item: PersistentTodoItem) {
        todoItemDao.insert(item)
    }

    suspend fun resetAllTaskCompletions() {
        todoItemDao.resetAllTaskCompletions()
    }
}

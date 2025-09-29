package com.example.dailyvisualschedule

import android.app.Application
import com.example.dailyvisualschedule.data.AppDatabase
import com.example.dailyvisualschedule.data.TaskDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class VisualScheduleApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    // Removed applicationScope from getDatabase call
    val database by lazy { AppDatabase.getDatabase(this) }
    
    val taskDataRepository by lazy { 
        TaskDataRepository(database.todoItemDao(), database.redeemedRewardDao(), this)
    }

    override fun onCreate() {
        super.onCreate()
        checkAndResetTaskCompletions()
    }

    private fun checkAndResetTaskCompletions() {
        applicationScope.launch {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            sdf.timeZone = TimeZone.getTimeZone("America/New_York") // Eastern Time
            val currentDateInET = sdf.format(Date())

            val lastResetDate = taskDataRepository.getLastResetDate()

            if (currentDateInET != lastResetDate) {
                taskDataRepository.resetAllTaskCompletions()
                taskDataRepository.setLastResetDate(currentDateInET)
            }
        }
    }
}

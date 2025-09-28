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

    val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val taskDataRepository by lazy { TaskDataRepository(database.todoItemDao(), this) }

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
                // Optionally, log this event or show a subtle notification if needed,
                // but for a background process, silent operation is usually best.
            }
        }
    }
}

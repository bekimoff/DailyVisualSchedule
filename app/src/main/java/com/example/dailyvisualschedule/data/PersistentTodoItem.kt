package com.example.dailyvisualschedule.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.annotation.DrawableRes

@Entity(tableName = "persistent_todo_items")
data class PersistentTodoItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val taskName: String,
    @DrawableRes val imageResId: Int,
    var isEllieCompleted: Boolean = false,
    var isAdaCompleted: Boolean = false,
    val scheduleType: String // "day" or "night"
)

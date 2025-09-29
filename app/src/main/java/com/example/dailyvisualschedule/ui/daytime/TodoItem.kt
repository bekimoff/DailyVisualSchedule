package com.example.dailyvisualschedule.ui.daytime

import androidx.annotation.DrawableRes

data class TodoItem(
    val id: Int, // Added id field from database
    val name: String,
    @DrawableRes val imageResId: Int,
    var isEllieCompleted: Boolean = false,
    var isAdaCompleted: Boolean = false
    // Removed orderIndex field
)

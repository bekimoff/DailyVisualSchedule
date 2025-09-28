package com.example.dailyvisualschedule.ui.daytime

import androidx.annotation.DrawableRes

data class TodoItem(
    @DrawableRes val imageResId: Int,
    var isEllieCompleted: Boolean = false,
    var isAdaCompleted: Boolean = false
)

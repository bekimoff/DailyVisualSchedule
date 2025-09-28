package com.example.dailyvisualschedule.data

import android.content.Context
import android.content.SharedPreferences

class StarCounterRepository(context: Context) {

    private val prefsName = "StarCountsPrefs"
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)

    companion object {
        // Keys for SharedPreferences
        private const val KEY_ELLIE_STARS = "ellie_stars"
        private const val KEY_ADA_STARS = "ada_stars"
    }

    fun getStarCount(userName: String): Int {
        return when (userName.lowercase()) {
            "ellie" -> sharedPreferences.getInt(KEY_ELLIE_STARS, 0)
            "ada" -> sharedPreferences.getInt(KEY_ADA_STARS, 0)
            else -> 0 // Or throw an IllegalArgumentException
        }
    }

    fun setStarCount(userName: String, count: Int) {
        when (userName.lowercase()) {
            "ellie" -> sharedPreferences.edit().putInt(KEY_ELLIE_STARS, count).apply()
            "ada" -> sharedPreferences.edit().putInt(KEY_ADA_STARS, count).apply()
            // else -> throw IllegalArgumentException("Unknown user: $userName")
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
        return currentCount // Or 0 if you prefer not to go below zero
    }

    // Optional: Function to reset counts
    fun resetStarCount(userName: String) {
        setStarCount(userName, 0)
    }
}

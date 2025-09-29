package com.example.dailyvisualschedule.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "redeemed_rewards")
data class RedeemedRewardEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val childName: String,
    val rewardDescription: String,
    val rewardPoints: Int,
    val redemptionTimestamp: Long, // Store as milliseconds since epoch
    var isFulfilled: Boolean = false
)
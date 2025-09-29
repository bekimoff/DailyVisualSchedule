package com.example.dailyvisualschedule.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RedeemedRewardDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(redeemedReward: RedeemedRewardEntry)

    @Update
    suspend fun update(redeemedReward: RedeemedRewardEntry)

    @Query("SELECT * FROM redeemed_rewards WHERE isFulfilled = 0 ORDER BY redemptionTimestamp DESC")
    fun getUnfulfilledRewards(): Flow<List<RedeemedRewardEntry>>

    // Optional: If you want a way to clear fulfilled rewards from the parent list
    @Query("DELETE FROM redeemed_rewards WHERE id = :id")
    suspend fun deleteById(id: Int)

    // Optional: if you want to get all rewards ever redeemed for some history/logging
    @Query("SELECT * FROM redeemed_rewards ORDER BY redemptionTimestamp DESC")
    fun getAllRedeemedRewards(): Flow<List<RedeemedRewardEntry>>
}
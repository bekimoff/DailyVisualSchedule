package com.example.dailyvisualschedule.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: PersistentTodoItem)

    @Insert(onConflict = OnConflictStrategy.IGNORE) // Ignore if item with same PK already exists
    suspend fun insertAll(items: List<PersistentTodoItem>)

    @Update
    suspend fun update(item: PersistentTodoItem)

    // Reverted to order by id ASC
    @Query("SELECT * FROM persistent_todo_items WHERE scheduleType = :scheduleType ORDER BY id ASC")
    fun getTasksByScheduleType(scheduleType: String): Flow<List<PersistentTodoItem>>

    @Query("SELECT COUNT(*) FROM persistent_todo_items")
    suspend fun getCount(): Int

    @Query("UPDATE persistent_todo_items SET isEllieCompleted = 0, isAdaCompleted = 0")
    suspend fun resetAllTaskCompletions()
}

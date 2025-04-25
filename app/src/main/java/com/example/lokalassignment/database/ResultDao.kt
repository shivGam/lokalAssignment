package com.example.lokalassignment.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.lokalassignment.model.Result

@Dao
interface ResultDao {
    // Insert or update a result.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(result: Result): Long

    // Delete a result.
    @Delete
    suspend fun delete(result: Result)

    @Query("SELECT * FROM results")
    fun getAllResults(): LiveData<List<Result>>
}

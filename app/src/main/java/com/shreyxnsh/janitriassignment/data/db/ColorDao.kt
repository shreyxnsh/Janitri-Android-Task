package com.shreyxnsh.janitriassignment.data.db

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface ColorDao {

    @Query("SELECT * from color_db")
    fun getAllColor():LiveData<List<ColorEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertColor(color: ColorEntity)

    @Delete
    suspend fun deleteColor(color: ColorEntity)

    @Query("SELECT COUNT(*) FROM color_db WHERE syncStatus = 0")
    fun getPendingSyncCount(): LiveData<Int>

    @Query("SELECT * FROM color_db WHERE syncStatus = 0")
    suspend fun getPendingSyncColors(): List<ColorEntity>

    @Query("UPDATE color_db SET syncStatus = :status WHERE id = :id")
    suspend fun setSyncStatus(id: String, status: Boolean)

    @Query("DELETE FROM color_db")
    suspend fun deleteAllColors()
}
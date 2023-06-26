package com.shreyxnsh.janitriassignment.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "color_db")
data class ColorEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo
    val color: String,
    @ColumnInfo
    val time: Long,
    @ColumnInfo(name = "syncStatus") val syncStatus: Boolean = false,
)
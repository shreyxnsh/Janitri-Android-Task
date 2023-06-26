package com.shreyxnsh.janitriassignment.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ColorEntity::class], version = 1)
abstract class ColorDatabase :RoomDatabase(){

    abstract fun colorDao(): ColorDao

    companion object{
        @Volatile
        private var instance : ColorDatabase?=null

        fun getInstance(context: Context): ColorDatabase {
            return instance ?: synchronized(this){
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    ColorDatabase::class.java,
                    "color_database"
                ).build().also { instance = it }
            }
        }
    }
}
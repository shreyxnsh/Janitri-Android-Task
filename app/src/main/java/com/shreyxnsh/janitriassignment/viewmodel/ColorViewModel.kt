package com.shreyxnsh.janitriassignment.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.shreyxnsh.janitriassignment.data.db.ColorDao
import com.shreyxnsh.janitriassignment.data.db.ColorDatabase
import com.shreyxnsh.janitriassignment.data.db.ColorEntity
import com.shreyxnsh.janitriassignment.data.repository.ColorRepository
import com.shreyxnsh.janitriassignment.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ColorViewModel(application: Application):AndroidViewModel(application) {

    private val colorDao: ColorDao
    private val colorRepository:ColorRepository
    val colors:LiveData<List<ColorEntity>>
    val pendingSyncCount:LiveData<Int>
    val colorsFromFirebase: LiveData<List<ColorEntity>>

    init {
        val database = ColorDatabase.getInstance(application)
        colorDao = database.colorDao()
        colorRepository = ColorRepository(application,colorDao)
        colors = colorDao.getAllColor()
        pendingSyncCount = colorDao.getPendingSyncCount()
        colorsFromFirebase = colorRepository.getColorsFromFirebase()
    }

    fun insertColor(context: Context, color : ColorEntity){
        viewModelScope.launch (Dispatchers.IO){
            colorRepository.insertColorToRoom(color)
        }
    }

    fun deleteAllData() {
        viewModelScope.launch {
            colorDao.deleteAllColors()
        }
    }

    fun syncColors() {
        viewModelScope.launch(Dispatchers.IO) {
            val pendingColors = colorDao.getPendingSyncColors()
            for (color in pendingColors) {
                colorRepository.insertColorToFirebase(color)
                colorRepository.setSyncStatus(color, true)
            }
        }
    }

}
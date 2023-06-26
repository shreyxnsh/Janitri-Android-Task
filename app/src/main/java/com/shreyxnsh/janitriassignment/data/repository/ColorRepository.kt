package com.shreyxnsh.janitriassignment.data.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.janitriassignment.data.db.ColorDao
import com.example.janitriassignment.data.db.ColorEntity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ColorRepository(private val context: Context,private val colorDao: ColorDao) {

    private val firebaseDatabase : FirebaseDatabase = FirebaseDatabase.getInstance()
    val pendingSyncCount: LiveData<Int> = colorDao.getPendingSyncCount()

    fun getColorsFromFirebase():LiveData<List<ColorEntity>>{
        val colorLiveData = MutableLiveData<List<ColorEntity>>()
        val colorRef  = firebaseDatabase.getReference("color_db")
        colorRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val colors = mutableListOf<ColorEntity>()
                for (childSnapshot in snapshot.children){
                    val id = childSnapshot.key ?: ""
                    val color = childSnapshot.child("color").getValue(String::class.java) ?: ""
                    val time = childSnapshot.child("time").getValue(Long::class.java) ?: 0L
                    val colorEntity = ColorEntity(id,color,time)
                    colors.add(colorEntity)
                }
                colorLiveData.postValue(colors)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Shreyansh", "Failed to read value.", error.toException())
            }

        })

        return colorLiveData
    }

    suspend fun insertColorToRoom(color: ColorEntity) {
        colorDao.insertColor(color)
    }

    suspend fun setSyncStatus(color: ColorEntity, status: Boolean) {
        colorDao.setSyncStatus(color.id, status)
    }


    fun insertColorToFirebase(color: ColorEntity){
        val colorRef = firebaseDatabase.getReference("color_db")
        val newRef  = colorRef.push()
        newRef.setValue(color){ databaseError, _ ->
            if (databaseError != null) {
                Toast.makeText(context,"Data Upload Failed!!",Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context,"Data Upload Successfully!!",Toast.LENGTH_SHORT).show()
            }
        }

    }
}
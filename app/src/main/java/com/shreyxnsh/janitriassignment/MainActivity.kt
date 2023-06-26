package com.shreyxnsh.janitriassignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridLayout
import android.widget.Toast
import android.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.shreyxnsh.janitriassignment.R
import com.shreyxnsh.janitriassignment.data.db.ColorEntity
import com.shreyxnsh.janitriassignment.databinding.ActivityMainBinding
import com.shreyxnsh.janitriassignment.ui.adapter.ColorAdapter
import kotlin.random.Random
import com.shreyxnsh.janitriassignment.viewmodel.ColorViewModel
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var colorViewModel: ColorViewModel
    private lateinit var colorAdapter: ColorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.cutomAppBar)

        colorAdapter = ColorAdapter()
        binding.apply {
            recyclerView.adapter = colorAdapter
            recyclerView.layoutManager = GridLayoutManager(this@MainActivity,2)
        }

        colorViewModel = ViewModelProvider(this)[ColorViewModel::class.java]
        colorViewModel.colors.observe(this) { colors ->
            colorAdapter.updateData(colors)
        }

        colorViewModel.pendingSyncCount.observe(this) { count ->
            binding.syncBtn.text = count.toString()
        }

        binding.syncBtn.setOnClickListener {
            colorViewModel.syncColors()
        }

        colorViewModel.colorsFromFirebase.observe(this) { colors ->
            colorAdapter.updateData(colors)
        }

        binding.deleteButton.setOnClickListener {
            colorViewModel.deleteAllData()
            Toast.makeText(this,"Data Deleted Successfully!!", Toast.LENGTH_SHORT).show()
        }

        binding.newColor.setOnClickListener {
            val randomColor = generateRandomColor()
            val currentTime = System.currentTimeMillis()
            val colorEntity = ColorEntity(UUID.randomUUID().toString(), randomColor, currentTime)
            colorViewModel.insertColor(this,colorEntity)
        }
    }

    private fun generateRandomColor(): String {
        val letters = "0123456789ABCDEF"
        val color = StringBuilder("#")

        // Generate random color hex code with 6 characters
        for (i in 0 until 6) {
            color.append(letters[Random.nextInt(letters.length)])
        }

        return color.toString()
    }
}
package com.shreyxnsh.janitriassignment.ui.adapter

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.shreyxnsh.janitriassignment.R
import com.shreyxnsh.janitriassignment.data.db.ColorEntity
import java.text.SimpleDateFormat
import java.util.*

class ColorAdapter : RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {

    private val colors : MutableList<ColorEntity> = mutableListOf()


    inner class ColorViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

        private val colorCode = itemView.findViewById<TextView>(R.id.color_code)
        private val colorTime = itemView.findViewById<TextView>(R.id.time_format)
        private val itemLayout = itemView.findViewById<RelativeLayout>(R.id.item_color_back)

        fun bind(color:ColorEntity){
            colorCode.text = color.color
            colorTime.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(color.time))
            itemLayout.setBackgroundColor(Color.parseColor(color.color))

            itemView.setOnClickListener {
                copyToClipboard(itemView.context,color.color)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ColorAdapter.ColorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_color, parent, false)
        return ColorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ColorAdapter.ColorViewHolder, position: Int) {
        val color = colors[position]
        holder.bind(color)
    }

    override fun getItemCount(): Int {
        return colors.size
    }

    fun updateData(newColors: List<ColorEntity>) {
        colors.clear()
        colors.addAll(newColors)
        notifyDataSetChanged()
    }

    private fun copyToClipboard(context: Context, text: String) {
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Color", text)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(context, "$text copied to clipboard", Toast.LENGTH_SHORT).show()
    }
}
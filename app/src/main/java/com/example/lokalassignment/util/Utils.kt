package com.example.lokalassignment.util

import android.content.Context
import android.widget.Toast
import androidx.core.app.NotificationCompat.MessagingStyle.Message

object Utils {
    fun showToast(message: String,context: Context){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
    }
}
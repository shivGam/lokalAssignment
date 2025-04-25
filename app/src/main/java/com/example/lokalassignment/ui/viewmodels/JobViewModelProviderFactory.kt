package com.example.lokalassignment.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lokalassignment.repository.JobRepository

class JobViewModelProviderFactory(
    val app : Application,
    val jobRepository: JobRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JobViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JobViewModel(app, jobRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
package com.example.lokalassignment.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.lokalassignment.R
import com.example.lokalassignment.api.JobApi
import com.example.lokalassignment.api.RetrofitInstance
import com.example.lokalassignment.database.ResultDatabase
import com.example.lokalassignment.repository.JobRepository
import com.example.lokalassignment.ui.viewmodels.JobViewModel
import com.example.lokalassignment.ui.viewmodels.JobViewModelProviderFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class JobsActivity : AppCompatActivity() {
    lateinit var viewModel:JobViewModel
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_jobs)

        val jobApi = RetrofitInstance.api

        val jobDatabase = ResultDatabase(this)
        val jobRepository = JobRepository(jobApi,jobDatabase)
        //val jobRepository = JobRepository(jobApi)
        val viewModelProviderFactory=JobViewModelProviderFactory(application, jobRepository)
        viewModel = ViewModelProvider(this,viewModelProviderFactory).get(JobViewModel::class.java)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

      val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.setupWithNavController(navController)
    }
}
package com.example.lokalassignment.api

import com.example.lokalassignment.model.JobResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface JobApi {
    @GET("common/jobs")
    suspend fun getJobs(
        @Query("page") page: Int=1
    ): Response<JobResponse>
}
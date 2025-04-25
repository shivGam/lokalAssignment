package com.example.lokalassignment.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.lokalassignment.database.Converters
import java.io.Serializable

@Entity(tableName = "results")
data class Result(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val amount: String?,
    val button_text: String?,
    val company_name: String?,
    val contact_preference: ContactPreference?,
    val content: String?,
    val contentV3: ContentV3?,
    val custom_link: String?,
    val experience: Int?,
    val fees_text: String?,
    var is_bookmarked: Boolean?,
    val job_location_slug: String?,
    val is_job_seeker_profile_mandatory: Boolean?,
    val job_tags: List<JobTag>?,
    val job_type: Int?,
    val primary_details: PrimaryDetails?,
    val qualification: Int?,
    val shift_timing: Int?,
    val status: Int?,
    val title: String?,
    val type: Int?,
    val whatsapp_no: String?
) : Serializable

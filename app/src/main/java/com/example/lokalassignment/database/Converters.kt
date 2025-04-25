package com.example.lokalassignment.database

import androidx.room.TypeConverter
import com.example.lokalassignment.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    private val gson = Gson()

    // For List<JobTag>
    @TypeConverter
    fun fromJobTagList(jobTags: List<JobTag>?): String = gson.toJson(jobTags)

    @TypeConverter
    fun toJobTagList(jobTagsJson: String?): List<JobTag>? {
        val listType = object : TypeToken<List<JobTag>>() {}.type
        return gson.fromJson(jobTagsJson, listType)
    }

    // For ContentV3
    @TypeConverter
    fun fromContentV3(contentV3: ContentV3?): String? {
        return gson.toJson(contentV3)
    }

    @TypeConverter
    fun toContentV3(contentV3Json: String?): ContentV3? {
        return gson.fromJson(contentV3Json, ContentV3::class.java)
    }

    // For PrimaryDetails
    @TypeConverter
    fun fromPrimaryDetails(primaryDetails: PrimaryDetails?): String? {
        return gson.toJson(primaryDetails)
    }

    @TypeConverter
    fun toPrimaryDetails(primaryDetailsJson: String?): PrimaryDetails? {
        return gson.fromJson(primaryDetailsJson, PrimaryDetails::class.java)
    }

    // For ContactPreference
    @TypeConverter
    fun fromContactPreference(contactPreference: ContactPreference?): String? {
        return gson.toJson(contactPreference)
    }

    @TypeConverter
    fun toContactPreference(contactPreferenceJson: String?): ContactPreference? {
        return gson.fromJson(contactPreferenceJson, ContactPreference::class.java)
    }

}

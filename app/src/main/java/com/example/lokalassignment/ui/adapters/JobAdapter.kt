package com.example.lokalassignment.ui.adapters

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lokalassignment.databinding.CardJobBinding
import com.example.lokalassignment.model.Result
import com.example.lokalassignment.util.Utils

class JobAdapter(
    private val onItemClick: (Result) -> Unit,
    private val onBookmarkClick: (Result, Boolean) -> Unit
) : ListAdapter<Result, JobAdapter.JobViewHolder>(JobDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val binding = CardJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class JobViewHolder(private val binding: CardJobBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(result: Result) {
            binding.apply {
                // Set job title
                tvJobTitle.text = result.title ?: "N/A"

                // Set salary
                tvSalary.text = "₹${result.amount ?: "Salary not specified"}"

                // Set company name
                tvCompany.text = result.company_name ?: "Unknown Company"

                // Set location
                tvLocation.text = result.job_location_slug ?: "No location provided"

                // Set call HR button text
                btnCallHR.text = result.button_text ?: "Contact HR"

                // Set fee charged
                tvFeeCharged.text = result.fees_text ?: "No fees"

                // Handle job tags
                val jobTags = result.job_tags
                if (jobTags != null && jobTags.isNotEmpty()) {
                    val firstTag = jobTags[0]
                    tvVacancies.text = firstTag.value ?: "No tags"
                    tvVacancies.setBackgroundColor(Color.parseColor(firstTag.bg_color ?: "#FFFFFF"))
                    tvVacancies.setTextColor(Color.parseColor(firstTag.text_color ?: "#000000"))
                } else {
                    tvVacancies.text = "No tags available"
                }

                btnChat.setOnClickListener {
                    val contactPreference = result.contact_preference
                    if (contactPreference?.preference == 1) {
                        val whatsappLink = contactPreference.whatsapp_link
                        if (whatsappLink?.isNotEmpty()==true) {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse(whatsappLink)
                            try {
                                itemView.context.startActivity(intent)
                            } catch (e: ActivityNotFoundException) {
                                Utils.showToast("WhatsApp is not installed on this device.", itemView.context)
                            }
                        }
                    }
                }

                if (result.is_bookmarked != null) {
                    btnBookmark.isChecked =
                        result.is_bookmarked!! // Assuming isBookmarked property exists in Result model
                    btnBookmark.setOnCheckedChangeListener { _, isChecked ->
                        onBookmarkClick(result, isChecked)
                    }
                } else {
                    btnBookmark.visibility = View.GONE
                }


                root.setOnClickListener { onItemClick(result) }
            }
        }
    }
}

class JobDiffCallback : DiffUtil.ItemCallback<Result>() {
    override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
        return oldItem == newItem
    }
}
package com.example.lokalassignment.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.lokalassignment.R
import com.example.lokalassignment.databinding.FragmentDetailJobBinding
import com.example.lokalassignment.model.Result
import com.example.lokalassignment.util.Utils


class DetailJobFragment : Fragment() {


    private var _binding: FragmentDetailJobBinding? = null
    private val binding get() = _binding!!

    private lateinit var result: Result

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailJobBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupListeners()
    }

    private fun setupListeners() {
        binding.chatButton.setOnClickListener {
            val contactPreference = result.contact_preference
            if (contactPreference?.preference == 1) {
                val whatsappLink = contactPreference.whatsapp_link
                if (whatsappLink?.isNotEmpty() == true) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(whatsappLink)
                    try {
                        context?.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        context?.let { it1 ->
                            Utils.showToast("WhatsApp is not installed on this device.",
                                it1
                            )
                        }
                    }
                }
            }
        }

        binding.callHrButton.setOnClickListener{
            val phoneNumber = "8097940776"
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phoneNumber")
            startActivity(intent)
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
            //findNavController().navigate(R.id.action_detailJobFragment_to_jobFragment)

        }

    }

    private fun setupViews() {
        result = arguments?.getSerializable("result") as? Result
            ?: throw IllegalArgumentException("Job details not found")

        with(binding) {
            jobTitle.text = result.title
            jobSalary.text = "₹${result.amount?: "Salary not specified"}"
            tvFeeText.text = if(result.fees_text.isNullOrEmpty()) "Fees not required" else result.fees_text
            companyName.text = result.company_name
            jobLocation.text = result.job_location_slug
            val jobTags = result.job_tags
            if (jobTags != null && jobTags.isNotEmpty()) {
                // Assuming you're showing the first tag in the list
                val firstTag = jobTags[0]
                vacanciesChip.text = "${firstTag.value ?: 0} Vacancies"
            } else {
                vacanciesChip.text = "No tags available"
            }
            tvExp.text = "Experience: ${result.primary_details?.Experience ?: "Not specified"}"
            tvQual.text = "Qualification: ${result.primary_details?.Qualification ?: "Not specified"}"
            val contentV3 = result.contentV3?.V3
            contentV3?.forEach { field ->
                when (field.field_key) {
                    "Gender" -> {
                        tvGender.text = "Gender: ${field.field_value ?: "Gender not specified"}"
                    }
                    "Shift timing" -> {
                        tvShift.text = "Shift timing: ${field.field_value ?: "Shift timing not specified"}"
                    }
                }
            }
            callHrButton.text = result.button_text ?: "Contact HR"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
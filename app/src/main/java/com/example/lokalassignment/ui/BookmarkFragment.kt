package com.example.lokalassignment.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lokalassignment.R
import com.example.lokalassignment.api.RetrofitInstance
import com.example.lokalassignment.database.ResultDatabase
import com.example.lokalassignment.databinding.FragmentBookmarkBinding
import com.example.lokalassignment.repository.JobRepository
import com.example.lokalassignment.ui.adapters.JobAdapter
import com.example.lokalassignment.ui.viewmodels.JobViewModel
import com.example.lokalassignment.ui.viewmodels.JobViewModelProviderFactory
import com.example.lokalassignment.util.Resource
import com.example.lokalassignment.util.Utils


class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: JobViewModel
    private lateinit var jobAdapter: JobAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val jobApi = RetrofitInstance.api
        val jobDatabase = ResultDatabase(requireContext())
        val factory = JobViewModelProviderFactory(requireActivity().application, JobRepository(jobApi,jobDatabase))
        //val factory = JobViewModelProviderFactory(requireActivity().application, JobRepository(jobApi))
        viewModel = ViewModelProvider(this, factory).get(JobViewModel::class.java)


        jobAdapter = JobAdapter(
            onItemClick = { result ->
                // Handle bookmark item click
                val bundle = Bundle().apply {
                    putSerializable("result", result)
                }
                findNavController().navigate(
                    R.id.action_bookmarkFragment_to_detailJobFragment,
                    bundle
                )
            },
            onBookmarkClick = { result, isBookmarked ->
                if (isBookmarked) {
                    viewModel.saveJob(result)
                    Utils.showToast("Job bookmarked", requireContext())
                } else {
                    viewModel.deleteJob(result)
                    Utils.showToast("Job removed from bookmarks", requireContext())
                    // Refresh the list after removing a bookmark
                    observeBookmarks()
                }
            }
        )

        setupRecyclerView()
        observeBookmarks()
    }

    private fun setupRecyclerView() {
        binding.rvBookmarkList.apply {
            adapter = jobAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeBookmarks() {
        viewModel.getBookmark().observe(viewLifecycleOwner) { bookmarkedJobs ->
            bookmarkedJobs?.let {
                hideProgressBar()
                if (it.isEmpty()) {
                    showEmptyState("No bookmarked jobs")
                } else {
                    // Hide the empty state and show the RecyclerView
                    binding.rvBookmarkList.visibility = View.VISIBLE
                    binding.tvEmptyState.visibility = View.GONE
                    jobAdapter.submitList(it)  // Submit the list of jobs to the adapter
                }
            } ?: run {
                showEmptyState("Error fetching bookmarks")
            }
        }
    }


    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showEmptyState(message: String) {
        binding.rvBookmarkList.visibility = View.GONE
        binding.tvEmptyState.apply {
            text = message
            visibility = View.VISIBLE
        }
        context?.let { Utils.showToast(message, it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.lokalassignment.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lokalassignment.R
import com.example.lokalassignment.api.RetrofitInstance
import com.example.lokalassignment.database.ResultDatabase
import com.example.lokalassignment.databinding.FragmentJobBinding
import com.example.lokalassignment.repository.JobRepository
import com.example.lokalassignment.ui.adapters.JobAdapter
import com.example.lokalassignment.ui.viewmodels.JobViewModel
import com.example.lokalassignment.ui.viewmodels.JobViewModelProviderFactory
import com.example.lokalassignment.util.Constants.Companion.QUERY_ITEM
import com.example.lokalassignment.util.Resource
import com.example.lokalassignment.util.Utils


class JobFragment : Fragment() {

    private var _binding: FragmentJobBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: JobViewModel
    private lateinit var jobAdapter: JobAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentJobBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val jobApi = RetrofitInstance.api
        val jobDatabase = ResultDatabase(requireContext())
        val factory = JobViewModelProviderFactory(requireActivity().application, JobRepository(jobApi,jobDatabase))
        //val factory = JobViewModelProviderFactory(requireActivity().application, JobRepository(jobApi))
        viewModel = ViewModelProvider(this, factory).get(JobViewModel::class.java)

        // Initialize the adapter
        jobAdapter = JobAdapter(
            onItemClick = { result ->
                val bundle = Bundle().apply {
                    putSerializable("result", result)
                }
                findNavController().navigate(
                    R.id.action_jobFragment_to_detailJobFragment,
                    bundle
                )
            },
            onBookmarkClick = { result, isBookmarked ->
                if (isBookmarked) {
                    viewModel.saveJob(result)
                    Utils.showToast("Job bookmarked", requireContext())
                } else {
//                    viewModel.deleteJob(result)
//                    Utils.showToast("Job removed from bookmarks", requireContext())
                }
            }
        )
        setupRecyclerView()
        observeJobs()
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollLister = object :RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling=true
            }

        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager=recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtStart = firstVisibleItemPosition >= 0
            val isTotalMore = totalItemCount >= QUERY_ITEM
            val shouldPaginate = isAtLastItem && isNotAtStart && isTotalMore && isNotLoadingNotLastPage && isScrolling
            if(shouldPaginate){
                viewModel.getJobs()
                isScrolling=false
            }

        }
    }

    private fun setupRecyclerView() {
        // Setup RecyclerView with the initialized adapter
        binding.rvJobList.apply {
            adapter = jobAdapter
            layoutManager = LinearLayoutManager(context)
            addOnScrollListener(this@JobFragment.scrollLister)
        }
    }

    private fun observeJobs() {
        viewModel.JobList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()  // Hide the progress bar when the request is successful

                    response.data?.let { jobResponse ->
                        val currentJobList = jobResponse.results

                        if (currentJobList.isNullOrEmpty()) {
                            showEmptyState("No jobs available")
                        } else {
                            // If the list is not empty, submit the new list of jobs to the adapter
                            jobAdapter.submitList(currentJobList.toList())

                            // Determine if it's the last page by checking if the fetched list size is smaller than the page size (QUERY_ITEM)
                            isLastPage = currentJobList.size < QUERY_ITEM
                            if (isLastPage) {
                                binding.rvJobList.setPadding(0, 0, 0, 0) // Adjust padding if it's the last page
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        println("Error: $message")
                        showEmptyState(message)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        isLoading = true
    }


    private fun showEmptyState(message: String) {
        binding.rvJobList.visibility = View.GONE
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

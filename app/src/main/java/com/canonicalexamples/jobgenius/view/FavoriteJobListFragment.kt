package com.canonicalexamples.jobgenius.view

import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.canonicalexamples.jobgenius.R
import com.canonicalexamples.jobgenius.app.JobGeniusApp
import com.canonicalexamples.jobgenius.databinding.ActivityJobListingBinding
import com.canonicalexamples.jobgenius.model.user.User
import com.canonicalexamples.jobgenius.util.SecureStorage
import com.canonicalexamples.jobgenius.viewmodels.JobListViewModel
import com.canonicalexamples.jobgenius.viewmodels.JobListViewModelFactory

class FavoriteJobListFragment : Fragment() {

    private lateinit var binding: ActivityJobListingBinding
    private val viewModel: JobListViewModel by viewModels {
        val app = activity?.application as JobGeniusApp
        JobListViewModelFactory(app.database, app.auth)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        binding = ActivityJobListingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val adapter = FavoriteJobListAdapter(viewModel)

        binding.jobList.recyclerView.adapter = adapter
        binding.jobList.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.fabJobList.observe(viewLifecycleOwner, { fabJob -> adapter.setData(fabJob) })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_login -> {
                findNavController().navigate(R.id.action_JobFavListingFragment_to_LoginFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

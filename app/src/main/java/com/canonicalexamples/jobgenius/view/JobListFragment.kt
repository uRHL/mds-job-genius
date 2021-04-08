package com.canonicalexamples.jobgenius.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.canonicalexamples.jobgenius.R
import com.canonicalexamples.jobgenius.app.JobGeniusApp
import com.canonicalexamples.jobgenius.databinding.FragmentJobListBinding
import com.canonicalexamples.jobgenius.util.observeEvent
import com.canonicalexamples.jobgenius.viewmodels.JobListViewModel
import com.canonicalexamples.jobgenius.viewmodels.JobListViewModelFactory

class JobListFragment : Fragment() {

    private lateinit var binding: FragmentJobListBinding
    private val viewModel: JobListViewModel by viewModels {
        val app = activity?.application as JobGeniusApp
        JobListViewModelFactory(app.database, app.webservice)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentJobListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = JobListAdapter(viewModel)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val jobViewModel = ViewModelProvider(this).get(JobListViewModel::class.java)
        jobViewModel.jobList.observe(viewLifecycleOwner, { job -> adapter.setData(job)})
//        binding.fab.setOnClickListener {
//            viewModel.addButtonClicked()
//        }

//        viewModel.navigate.observeEvent(viewLifecycleOwner) { navigate ->
//            if (navigate) {
//                //findNavController().navigate(R.id.action_JobListFragment_to_SearchFragment)
//            }
//        }
    }
}

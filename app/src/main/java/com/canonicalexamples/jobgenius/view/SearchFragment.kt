package com.canonicalexamples.jobgenius.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.canonicalexamples.jobgenius.R
import com.canonicalexamples.jobgenius.app.JobGeniusApp
import com.canonicalexamples.jobgenius.viewmodels.SearchViewModel
import com.canonicalexamples.jobgenius.viewmodels.SearchViewModelFactory
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.runBlocking


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModels {
        val app = activity?.application as JobGeniusApp
        SearchViewModelFactory(app.database, app.webservice)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.search_button).setOnClickListener {
            // I do not want to implement logic here, but in the viewmodel

            // We try to read user input (if not null)
            val searchFilters = view.findViewById<TextInputLayout>(R.id.job_search_filter).editText?.text
            val isRemote = view.findViewById<CheckBox>(R.id.checkbox_remote).isChecked
            println(searchFilters.toString())
            runBlocking {
                viewModel.onClickSearch(searchFilters.toString(),isRemote)
            }
            findNavController().navigate(R.id.action_SearchFragment_to_JobListFragment)
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_login -> {
                findNavController().navigate(R.id.action_SearchFragment_to_LoginFragment)
                true
            }

            R.id.action_favorites -> {
                findNavController().navigate(R.id.action_SearchFragment_to_JobFavListingFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}

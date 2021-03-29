package com.canonicalexamples.jobgenius.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.canonicalexamples.jobgenius.R
import com.canonicalexamples.jobgenius.model.JobFacts
import com.canonicalexamples.jobgenius.model.JobFactsService
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.search_button).setOnClickListener {
            // I do not want to implement logic here, but in the viewmodel

            // We try to read user input (if not null)
            val searchFilters = view.findViewById<TextInputLayout>(R.id.job_search_filter).editText?.text
            val isRemote = view.findViewById<CheckBox>(R.id.checkbox_remote).isChecked
            println("Pues este es su texto: $searchFilters\nY este su remote: $isRemote")

            // We create the service
            val retrofit = Retrofit.Builder()
                    .baseUrl("https://jobs.github.com/positions.json/")
                    .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                    .build()

            val service: JobFactsService = retrofit.create(JobFactsService::class.java)
            var location: String = ""
            if (isRemote){
                location = "Remote"
            }

            //we make the call
            val jobs: Call<List<JobFacts>> = service.getJobs(1, searchFilters.toString(), location)

            //println(jobs.await().size)


            // To navigate, the action must be defined in the navigation graph XML
            findNavController().navigate(R.id.action_SearchFragment_to_JobListFragment)
        }
    }
}

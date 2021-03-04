package com.canonicalexamples.mypantry.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.canonicalexamples.mypantry.R
import com.canonicalexamples.mypantry.app.MyPantryApp
import com.canonicalexamples.mypantry.databinding.FragmentFoodListBinding
import com.canonicalexamples.mypantry.util.observeEvent
import com.canonicalexamples.mypantry.viewmodels.FoodsListViewModel
import com.canonicalexamples.mypantry.viewmodels.FoodsListViewModelFactory

class FoodListFragment : Fragment() {

    private lateinit var binding: FragmentFoodListBinding
    private val viewModel: FoodsListViewModel by viewModels {
        val app = activity?.application as MyPantryApp
        FoodsListViewModelFactory(app.database, app.webservice)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFoodListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = FoodListAdapter(viewModel = viewModel)
        binding.fab.setOnClickListener {
            viewModel.addButtonClicked()
        }

        viewModel.navigate.observeEvent(viewLifecycleOwner) { navigate ->
            if (navigate) {
                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            }
        }
    }
}

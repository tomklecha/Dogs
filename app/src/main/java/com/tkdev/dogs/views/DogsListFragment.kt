package com.tkdev.dogs.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tkdev.dogs.R
import com.tkdev.dogs.adapters.DogsListAdapter
import com.tkdev.dogs.model.DogModel
import com.tkdev.dogs.databinding.FragmentListDogsBinding
import com.tkdev.dogs.viewmodels.DogsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DogsListFragment : Fragment() {

    private val dogsViewModel: DogsViewModel by activityViewModels()

    private lateinit var dogsListAdapter: DogsListAdapter
    private lateinit var binding: FragmentListDogsBinding

    interface Callback {
        fun showBreedPictures(dogModel: DogModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        binding = FragmentListDogsBinding
            .inflate(inflater, container, false).apply {
                viewModel = dogsViewModel
                lifecycleOwner = viewLifecycleOwner
                callback = object : Callback {
                    override fun showBreedPictures(dogModel: DogModel) {
                        val bundle = Bundle()
                        bundle.putParcelable(SELECTED_DOG_MODEL, dogModel)
                        findNavController().navigate(
                            R.id.action_listFragment_to_breedFragment,
                            bundle
                        )
                    }
                }
            }

        dogsListAdapter = DogsListAdapter(binding.callback)

        binding.dogsListRecyclerView.apply {
            adapter = dogsListAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        binding.dogsListSwipeLayout.setOnRefreshListener {
            dogsViewModel.getDogsList()
        }

        binding.dogsListRecyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )

        dogsViewModel.setToolbarTitle(getString(R.string.app_name))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dogsViewModel.dogsList.observe(
            viewLifecycleOwner,
            { dogsList ->
                dogsListAdapter.submitList(dogsList)
            }
        )

        dogsViewModel.isDogListRefreshing.observe(
            viewLifecycleOwner,
            { isRefreshing ->
                binding.dogsListSwipeLayout.isRefreshing = isRefreshing
            }
        )

        dogsViewModel.emptyListVisibility.observe(
            viewLifecycleOwner,
            { isVisible ->
                if (isVisible) dogsViewModel.getDogsList()
            }
        )

    }
}
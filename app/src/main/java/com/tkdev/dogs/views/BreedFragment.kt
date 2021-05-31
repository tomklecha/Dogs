package com.tkdev.dogs.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.tkdev.dogs.R
import com.tkdev.dogs.adapters.BreedAdapter
import com.tkdev.dogs.model.DogModel
import com.tkdev.dogs.databinding.FragmentBreedBinding
import com.tkdev.dogs.viewmodels.DogsViewModel
import dagger.hilt.android.AndroidEntryPoint

const val SELECTED_DOG_MODEL = "dog_model"

@AndroidEntryPoint
class BreedFragment : Fragment() {

    private val dogsViewModel: DogsViewModel by activityViewModels()

    private lateinit var binding: FragmentBreedBinding
    private lateinit var breedAdapter: BreedAdapter
    private var dogModel: DogModel? = null

    interface Callback {
        fun showPicture(imageUrl: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        dogModel = arguments?.getParcelable(SELECTED_DOG_MODEL) as? DogModel

        dogsViewModel.getBreedDogPictures(dogModel)

        binding = FragmentBreedBinding
            .inflate(inflater, container, false).apply {
                viewModel = dogsViewModel
                lifecycleOwner = viewLifecycleOwner
                callback = object : Callback {
                    override fun showPicture(imageUrl: String) {
                        val bundle = Bundle()
                        bundle.putString(IMAGE_URL, imageUrl)
                        findNavController().navigate(
                            R.id.action_breedFragment_to_pictureFragment,
                            bundle
                        )
                    }
                }
            }

        breedAdapter = BreedAdapter(binding.callback)

        binding.breedPicturesRecyclerView.apply {
            adapter = breedAdapter
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        }

        binding.breedPicturesRefreshLayout.setOnRefreshListener {
            dogsViewModel.getBreedDogPictures(dogModel)
        }

        dogsViewModel.setToolbarTitle(dogModel?.dogDisplayName)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dogsViewModel.breedDogPictures.observe(
            viewLifecycleOwner,
            {
                breedAdapter.submitList(it.peekContent())
            }
        )

        dogsViewModel.isDogListRefreshing.observe(
            viewLifecycleOwner,
            { isRefreshing ->
                binding.breedPicturesRefreshLayout.isRefreshing = isRefreshing
            }
        )
    }
}
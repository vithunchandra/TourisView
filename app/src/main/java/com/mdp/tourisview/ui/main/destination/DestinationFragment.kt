package com.mdp.tourisview.ui.main.destination

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.button.MaterialButton
import com.mdp.tourisview.R
import com.mdp.tourisview.databinding.FragmentDestinationBinding
import com.mdp.tourisview.ui.main.history.HistoryFragmentViewModelFactory
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "destination"

/**
 * A simple [Fragment] subclass.
 * Use the [DestinationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DestinationFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private val args: DestinationFragmentArgs by navArgs()
    private lateinit var binding: FragmentDestinationBinding
    private val viewModel:DestinationFragmentViewModel by viewModels{
        DestinationFragmentViewModelFactory.getInstance(requireActivity().baseContext)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_destination, container, false)
        Picasso.get()
            .load(args.destination.imageUrl)
            .into(binding.destinationIv)
        binding.viewModel = viewModel
        viewModel.data.value = args.destination
        if(args.destination.isBookmarked){
            (binding.bookmarkButton as MaterialButton).setIconResource(R.drawable.bookmark_filled_24px)
        }else{
            (binding.bookmarkButton as MaterialButton).setIconResource(R.drawable.bookmark_24px)
        }
        binding.bookmarkButton.setOnClickListener {
            viewModel.toggleBookmark()
            val updatedBookmark = !viewModel.data.value!!.isBookmarked
            viewModel.data.value = viewModel.data.value!!.copy(
                isBookmarked = updatedBookmark
            )

            if(updatedBookmark){
                (binding.bookmarkButton as MaterialButton).setIconResource(R.drawable.bookmark_filled_24px)
            }else{
                (binding.bookmarkButton as MaterialButton).setIconResource(R.drawable.bookmark_24px)
            }
        }
        return binding.root
    }

}
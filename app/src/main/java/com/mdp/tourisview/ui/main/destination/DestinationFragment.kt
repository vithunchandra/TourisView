package com.mdp.tourisview.ui.main.destination

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.mdp.tourisview.R
import com.mdp.tourisview.adapter.DestinationAdapter
import com.mdp.tourisview.adapter.ReviewAdapter
import com.mdp.tourisview.databinding.FragmentDestinationBinding
import com.mdp.tourisview.ui.main.history.HistoryFragmentViewModelFactory
import com.squareup.picasso.Picasso

class DestinationFragment : Fragment() {
    private val args: DestinationFragmentArgs by navArgs()
    private lateinit var binding: FragmentDestinationBinding
    private val viewModel:DestinationFragmentViewModel by viewModels{
        DestinationFragmentViewModelFactory.getInstance(requireActivity().baseContext)
    }
    private var adapter: ReviewAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_destination, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpState()
        setUpView()
        setUpAction()
    }

    private fun setUpView(){
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

        adapter = ReviewAdapter()
        binding.destinationRv.layoutManager = LinearLayoutManager(requireActivity().baseContext)
        binding.destinationRv.adapter = adapter
    }

    private fun setUpState(){
        viewModel.viewState.observe(viewLifecycleOwner){ state ->
            when{
                state.isSuccess -> {
                    adapter?.submitList(state.data)
//                    if(state.isSuccess && state.data!!.isEmpty()){
//                        Toast.makeText(context, "You haven't posted any destinations!", Toast.LENGTH_LONG).show()
//                    }
                }
                state.isError -> {
                    Toast.makeText(context, state.errorMessage, Toast.LENGTH_LONG).show()
                }
            }

//            if(state.isLoading) binding.progressBar.visibility = View.VISIBLE
//            else binding.progressBar.visibility = View.INVISIBLE
        }
    }

    private fun setUpAction(){
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

        binding.destinationSendButton.setOnClickListener {
            if (binding.destinationTn.text.toString() != ""){
                if (binding.destinationTn.text.toString().toInt() in 1..5){
                    viewModel.sendReview(args.destination.id, binding.destinationPt.text.toString(), binding.destinationTn.text.toString().toInt())
                    binding.destinationPt.setText("")
                    binding.destinationTn.setText("")
                    viewModel.getAllReview(args.destination.id)
                }else{
                    Toast.makeText(requireContext(), "Please rate 1 to 5", Toast.LENGTH_SHORT).show()
                    binding.destinationTn.setText("")
                }
            }else{
                Toast.makeText(requireContext(), "Please at least fill rate field", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllReview(args.destination.id)
    }
}
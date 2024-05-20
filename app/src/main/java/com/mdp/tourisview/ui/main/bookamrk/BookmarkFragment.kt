package com.mdp.tourisview.ui.main.bookamrk

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.mdp.tourisview.adapter.BookmarkedDestinationAdapter
import com.mdp.tourisview.data.local.room.model.RoomDestination
import com.mdp.tourisview.databinding.FragmentBookmarkBinding

class BookmarkFragment : Fragment() {
    private lateinit var binding: FragmentBookmarkBinding
    private val viewModel by viewModels<BookmarkFragmentViewModel> {
        BookmarkFragmentViewModelFactory.getInstance(requireActivity().baseContext)
    }
    private lateinit var adapter: BookmarkedDestinationAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpView()
        setUpState()
    }

    private fun setUpView(){
        adapter = BookmarkedDestinationAdapter(
            requireActivity().baseContext,
            object: BookmarkedDestinationAdapter.Action{
                override fun bookmarkClicked(destination: RoomDestination) {
                    viewModel.toggleBookmark(destination.id)
                }
            }
        )
        val layout = LinearLayoutManager(requireActivity().baseContext)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = layout
    }

    private fun setUpState(){
        viewModel.getBookmarkedDestinations().observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
    }
}
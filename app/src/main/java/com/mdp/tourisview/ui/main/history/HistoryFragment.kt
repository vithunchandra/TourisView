package com.mdp.tourisview.ui.main.history

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdp.tourisview.R
import com.mdp.tourisview.adapter.DestinationAdapter
import com.mdp.tourisview.adapter.HistoryDestinationAdapter
import com.mdp.tourisview.data.local.room.model.RoomDestination
import com.mdp.tourisview.data.mock.server.model.MockServerDestination
import com.mdp.tourisview.databinding.FragmentHistoryBinding
import com.mdp.tourisview.databinding.FragmentHomeBinding
import com.mdp.tourisview.ui.main.destination.DestinationFragmentData
import com.mdp.tourisview.ui.main.home.HomeFragmentDirections
import com.mdp.tourisview.ui.main.home.HomeFragmentViewModel
import com.mdp.tourisview.ui.main.home.HomeFragmentViewModelFactory
import com.mdp.tourisview.ui.main.map.view_location.DestinationMapLocation

class HistoryFragment : Fragment() {

    companion object {
        fun newInstance() = HistoryFragment()
    }

    private lateinit var binding:FragmentHistoryBinding
    private var adapter: HistoryDestinationAdapter? = null

    private val viewModel by viewModels<HistoryFragmentViewModel> {
        HistoryFragmentViewModelFactory.getInstance(requireActivity().baseContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpState()
        setUpView()
        setUpAction()
    }

    private fun setUpView(){
        adapter = HistoryDestinationAdapter(
            object: HistoryDestinationAdapter.Action {
                override fun onClick(mockServerDestination: MockServerDestination) {
                    val action = HistoryFragmentDirections.actionHistoryFragmentToDestinationFragment(
                        DestinationFragmentData.fromMockServerDestination(mockServerDestination)
                    )

                    findNavController().navigate(action)
                }

                override fun bookmarkClick(mockServerDestination: MockServerDestination) {
                    viewModel.toggleBookmark(mockServerDestination.id)
                }
            }
        )
        val layout = LinearLayoutManager(requireActivity().baseContext)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = layout

    }

    private fun setUpState(){
        viewModel.viewState.observe(viewLifecycleOwner){ state ->

            when{
                state.isSuccess -> {
                    adapter?.submitList(state.data)
                    if(state.isSuccess && state.data!!.isEmpty()){
                        Toast.makeText(context, "You haven't posted any destinations!", Toast.LENGTH_LONG).show()
                    }
                }
                state.isError -> {
                    Toast.makeText(context, "fetch failed!", Toast.LENGTH_LONG).show()
                }
            }

            if(state.isLoading) binding.progressBar.visibility = View.VISIBLE
            else binding.progressBar.visibility = View.INVISIBLE


        }
    }

    private fun setUpAction(){


    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllHistory()
    }
}
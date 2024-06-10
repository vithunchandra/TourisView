package com.mdp.tourisview.ui.main.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdp.tourisview.R
import com.mdp.tourisview.adapter.DestinationAdapter
import com.mdp.tourisview.data.local.room.model.RoomDestination
import com.mdp.tourisview.databinding.FragmentHomeBinding
import com.mdp.tourisview.ui.main.destination.DestinationFragmentData
import com.mdp.tourisview.ui.main.map.view_location.DestinationMapLocation

class HomeFragment : Fragment(){
    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeFragmentViewModel> {
        HomeFragmentViewModelFactory.getInstance(requireActivity().baseContext)
    }
    private var adapter: DestinationAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpState()
        setUpView()
        setUpAction()
    }

    private fun setUpView(){
        adapter = DestinationAdapter(
            object: DestinationAdapter.Action {
                override fun onClick(roomDestination: RoomDestination) {
                    val action = HomeFragmentDirections.actionHomeFragmentToDestinationFragment(
                        DestinationFragmentData.fromRoomDestination(roomDestination)
                    )

                    findNavController().navigate(action)
                }

                override fun bookmarkClick(roomDestination: RoomDestination) {
                    viewModel.toggleBookmark(roomDestination.id)
                }
            }
        )
        val layout = LinearLayoutManager(requireActivity().baseContext)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = layout

        binding.searchBar.inflateMenu(R.menu.home_menu)
        binding.searchView.setupWithSearchBar(binding.searchBar)
    }

    private fun setUpState(){
        viewModel.getAllDestinations().observe(viewLifecycleOwner){ state ->
            adapter?.submitList(state)
        }
    }

    private fun setUpAction(){
        binding.searchView.editText.setOnEditorActionListener { _, _, _ ->
            val name = binding.searchView.text.toString()
            binding.searchBar.setText(name)
            binding.searchView.hide()
            viewModel.getAllDestinations(name).observe(viewLifecycleOwner){ state ->
                adapter?.submitList(state)
            }
            false
        }

        binding.searchBar.setOnMenuItemClickListener{
            when(it.itemId){
                R.id.destination_location_menu -> {
                    val intent = Intent(requireActivity().baseContext, DestinationMapLocation::class.java)
                    startActivity(intent)
                    true
                }
                else -> {true}
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllDestinations()
    }

    private fun showToast(text: String){
        Toast.makeText(requireActivity().baseContext, text, Toast.LENGTH_SHORT).show()
    }
}
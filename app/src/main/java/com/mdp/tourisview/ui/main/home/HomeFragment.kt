package com.mdp.tourisview.ui.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdp.tourisview.R
import com.mdp.tourisview.adapter.DestinationAdapter
import com.mdp.tourisview.data.mock.model.Destination
import com.mdp.tourisview.databinding.FragmentHomeBinding

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
    }

    private fun setUpView(){
        adapter = DestinationAdapter(
            requireContext().applicationContext,
            object: DestinationAdapter.Action {
                override fun onClick(destination: Destination) {

                }
            }
        )
        val layout = LinearLayoutManager(requireActivity().baseContext)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = layout

        binding.searchView.setupWithSearchBar(binding.searchBar)
    }

    private fun setUpState(){
        viewModel.viewState.observe(viewLifecycleOwner){ state ->
            binding.progressBar.visibility = if(state.isLoading){
                View.VISIBLE
            }else{
                View.INVISIBLE
            }

            when{
                state.isError -> showToast(getString(R.string.error))
                state.isSuccess -> {
                    adapter?.submitList(state.data)
                }
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
package com.mdp.tourisview.ui.main.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mdp.tourisview.R
import com.mdp.tourisview.databinding.FragmentProfileBinding
import com.mdp.tourisview.ui.authorization.AuthorizationActivity

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel by viewModels<ProfileFragmentViewModel> {
        ProfileFragmentViewModelFactory.getInstance(requireActivity())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpState()
        setUpView()
    }

    private fun setUpState(){
        viewModel.getSession().observe(viewLifecycleOwner){
            if(it.isLogin){
                binding.displayNameTv.text = it.displayName
            }
        }
    }

    private fun setUpView(){
        binding.logoutButton.setOnClickListener {
            viewModel.logout()
        }
        binding.bookmarkButton.setOnClickListener {
            val directions = ProfileFragmentDirections.actionProfileFragmentToBookmarkFragment()
            findNavController().navigate(directions)
        }
    }
}
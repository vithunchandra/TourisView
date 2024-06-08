package com.mdp.tourisview.ui.authorization.signup

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mdp.tourisview.R
import com.mdp.tourisview.databinding.FragmentSignupBinding
import com.mdp.tourisview.util.TextChangedListener

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private val viewModel by viewModels<SignUpViewModel> {
        SignUpViewModelFactory.getInstance()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpState()
        setUpField()
        setUpAction()
    }

    private fun setUpState(){
        viewModel.viewState.observe(requireActivity()){
            if(it.isLoading){
                binding.progressBar.visibility = View.VISIBLE
                binding.signupButton.text = ""
            }else{
                binding.progressBar.visibility = View.GONE
                binding.signupButton.text = getString(R.string.signup)
            }

            when{
                it.isError -> showToast(it.errorMessage)
                it.data != null -> {
                    AlertDialog.Builder(requireActivity()).apply{
                        setTitle("Hooray!")
                        setMessage("Account registered successfully. Login and find your destination now")
                        setPositiveButton("Next"){ _, _ ->
                            val directions = SignUpFragmentDirections.actionSignupFragmentToSigninFragment()
                            findNavController().navigate(directions)
                        }
                        create()
                        show()
                    }
                }
            }
        }
    }

    private fun setUpField(){
//        binding.emailEt.setText(viewModel.email)
//        binding.nameEt.setText(viewModel.displayName)
//        binding.passwordEt.setText(viewModel.password)
//
//        binding.emailEt.addTextChangedListener(
//            object: TextChangedListener() {
//                override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
//                    viewModel.email = text.toString()
//                }
//            }
//        )
//        binding.nameEt.addTextChangedListener(
//            object: TextChangedListener() {
//                override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
//                    viewModel.displayName = text.toString()
//                }
//            }
//        )
//        binding.passwordEt.addTextChangedListener(
//            object: TextChangedListener() {
//                override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
//                    viewModel.password = text.toString()
//                }
//            }
//        )
    }

    private fun setUpAction(){
        binding.signinNavigation.setOnClickListener {
            val directions = SignUpFragmentDirections.actionSignupFragmentToSigninFragment()
            findNavController().navigate(directions)
        }

        binding.signupButton.setOnClickListener {
            viewModel.signup()
        }
    }

    private fun showToast(text: String){
        Toast.makeText(requireActivity(), text, Toast.LENGTH_SHORT).show()
    }
}
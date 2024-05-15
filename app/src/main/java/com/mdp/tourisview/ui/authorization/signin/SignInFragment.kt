package com.mdp.tourisview.ui.authorization.signin

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mdp.tourisview.R
import com.mdp.tourisview.databinding.FragmentSigninBinding
import com.mdp.tourisview.ui.main.MainActivity
import com.mdp.tourisview.util.TextChangedListener

class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSigninBinding
    private val viewModel by viewModels<SignInViewModel>{
        SignInViewModelFactory.getInstance(requireActivity())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSigninBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpState()
        setUpField()
        setUpAction()
    }

    private fun setUpState(){
        viewModel.getSession().observe(requireActivity()){
            if(it.isLogin){
                val intent = Intent(requireActivity(), MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                findNavController().popBackStack()
            }
        }

        viewModel.viewState.observe(requireActivity()){
            when(it.isLoading){
                true -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.signupButton.text = ""
                }
                false -> {
                    binding.progressBar.visibility = View.GONE
                    binding.signupButton.text = getString(R.string.signin)
                }
            }

            when{
                it.isError -> showToast(it.errorMessage)
                it.data != null -> {
                    AlertDialog.Builder(requireActivity()).apply {
                        setTitle("Yeah!")
                        setMessage("Login success, search your tourist destination now!")
                        setPositiveButton("Next"){ _, _ ->
                            val intent = Intent(requireActivity(), MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            findNavController().popBackStack()
                        }
                        create()
                        show()
                    }
                }
            }
        }
    }

    private fun setUpField(){
        binding.emailEt.setText(viewModel.email)
        binding.passwordEt.setText(viewModel.password)

        binding.emailEt.addTextChangedListener(
            object: TextChangedListener(){
                override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                    viewModel.email = text.toString()
                }
            }
        )
        binding.passwordEt.addTextChangedListener(
            object: TextChangedListener(){
                override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                    viewModel.password = text.toString()
                }
            }
        )
    }

    private fun setUpAction(){
        binding.signupNavigation.setOnClickListener {
            val directions = SignInFragmentDirections.actionSigninFragmentToSignupFragment()
            findNavController().navigate(directions)
        }

        binding.signupButton.setOnClickListener {
            viewModel.login()
        }
    }

    private fun showToast(text: String){
        Toast.makeText(requireActivity(), text, Toast.LENGTH_SHORT).show()
    }
}
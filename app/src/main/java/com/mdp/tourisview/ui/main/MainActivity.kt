package com.mdp.tourisview.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.mdp.tourisview.R
import com.mdp.tourisview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpView()
    }
    private fun setUpView(){
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val navController = binding.navHostFragment.getFragment<Fragment>().findNavController()
            when(item.itemId){
                R.id.home -> {
                    navController.navigate(R.id.action_global_homeFragment)
                    true
                }
                R.id.add -> {
                    navController.navigate(R.id.action_global_uploadFragment)
                    true
                }
                R.id.profile -> {
                    navController.navigate(R.id.action_global_profileFragment)
                    true
                }

                else -> true
            }
        }
    }
}
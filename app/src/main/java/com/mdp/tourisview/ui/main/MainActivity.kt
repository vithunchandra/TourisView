package com.mdp.tourisview.ui.main

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.mdp.tourisview.R
import com.mdp.tourisview.databinding.ActivityMainBinding
import com.mdp.tourisview.ui.authorization.AuthorizationActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainActivityViewModel>{
        MainActivityViewModelFactory.getInstance(this)
    }
    private val permissionsArray = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.CAMERA
    )

    private val requestBackgroundLocationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){ isGranted ->
        if(isGranted){
            showToast("Permissions granted")
        }else{
            showToast("Permissions rejected")
        }
    }

    private val requestMultiplePermission = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ){ permissions ->
        for (permission in permissions){
            if(permission.value){
                if(permission.key == Manifest.permission.ACCESS_FINE_LOCATION){
                    requestBackgroundLocationPermission.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                }
                showToast("${permission.key} permission granted")
            }else{
                showToast("${permission.key} permission rejected")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpState()
        setUpView()
        requestMultiplePermission.launch(permissionsArray)
    }

    private fun setUpState(){
        viewModel.getSession().observe(this){
            if(!it.isLogin){
                val intent = Intent(this, AuthorizationActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
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
    private fun showToast(text: String){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}
package com.mdp.tourisview.ui.main.upload

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mdp.tourisview.R
import com.mdp.tourisview.databinding.FragmentUploadBinding
import com.mdp.tourisview.ui.main.camera.CameraActivity
import com.mdp.tourisview.ui.main.map.select_location.SelectLocationActivity
import com.mdp.tourisview.util.TextChangedListener

class UploadFragment : Fragment() {
    private lateinit var binding: FragmentUploadBinding
    private val viewModel by viewModels<UploadFragmentViewModel>{
        UploadFragmentViewModelFactory.getInstance(requireActivity().baseContext)
    }

    private val registerForLocationResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode == SelectLocationActivity.LOCATION_RESULT){
            val latitude = it.data?.getDoubleExtra(SelectLocationActivity.LATITUDE, 0.0)
            val longitude = it.data?.getDoubleExtra(SelectLocationActivity.LONGITUDE, 0.0)

            binding.latitudeEt.setText(latitude.toString())
            binding.longitudeEt.setText(longitude.toString())
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ){ uri: Uri? ->
        if(uri != null){
            viewModel.setImageUri(uri)
        }else{
            showToast(getString(R.string.empty_gallery_result))
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode == CameraActivity.CAMERAX_RESULT){
            val uri: Uri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()!!
            viewModel.setImageUri(uri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUploadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpState()
        setUpField()
        setUpAction()
    }

    private fun setUpField(){
        binding.nameEt.addTextChangedListener(
            object: TextChangedListener(){
                override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                    viewModel.setName(text.toString())
                }
            }
        )

        binding.descriptionEt.addTextChangedListener(
            object: TextChangedListener(){
                override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                    viewModel.setDescription(text.toString())
                }
            }
        )

        binding.latitudeEt.addTextChangedListener(
            object: TextChangedListener(){
                override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                    if(text?.isNotEmpty() == true){
                        viewModel.setLatitude(text.toString().toDouble())
                    }
                }
            }
        )

        binding.longitudeEt.addTextChangedListener(
            object: TextChangedListener(){
                override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                    if(text?.isNotEmpty() == true){
                        viewModel.setLongitude(text.toString().toDouble())
                    }
                }
            }
        )
    }

    private fun setUpAction(){
        binding.galleryButton.setOnClickListener {
            launcherGallery.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }

        binding.cameraButton.setOnClickListener {
            val intent = Intent(requireActivity(), CameraActivity::class.java)
            launcherIntentCameraX.launch(intent)
        }

        binding.selectCoordinateButton.setOnClickListener {
            val intent = Intent(requireContext(), SelectLocationActivity::class.java)
            registerForLocationResult.launch(intent)
        }

        binding.uploadButton.setOnClickListener {
            viewModel.upload(requireActivity().applicationContext)
        }
    }

    private fun setUpState(){
        viewModel.viewState.observe(requireActivity()){ state ->
            if(state.imageUri != null){
                binding.imageViewer.setImageURI(state.imageUri)
            }

            if(state.isLoading){
                binding.progressBar.visibility = View.VISIBLE
                binding.uploadButton.isEnabled = false
                binding.uploadButton.text = ""
            }else{
                binding.progressBar.visibility = View.INVISIBLE
                binding.uploadButton.isEnabled = true
                binding.uploadButton.text = getString(R.string.upload)
            }

            when{
                state.isError -> showToast(state.errorMessage)
                state.isSuccess -> {
                    val directions = UploadFragmentDirections.actionGlobalHomeFragment()
                    showToast("Upload Success")
                    findNavController().navigate(directions)
                }
            }
        }
    }

    private fun showToast(text: String){
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }
}
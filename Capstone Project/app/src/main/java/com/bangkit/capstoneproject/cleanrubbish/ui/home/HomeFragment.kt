package com.bangkit.capstoneproject.cleanrubbish.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.capstoneproject.cleanrubbish.R
import com.bangkit.capstoneproject.cleanrubbish.adapter.ArticleAdapter
import com.bangkit.capstoneproject.cleanrubbish.data.utils.getImageUri
import com.bangkit.capstoneproject.cleanrubbish.databinding.FragmentHomeBinding
import com.bangkit.capstoneproject.cleanrubbish.helper.HomeViewModelFactory
import com.bangkit.capstoneproject.cleanrubbish.ui.result.ResultActivity
import com.yalantis.ucrop.UCrop

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ArticleAdapter
    private var currentImageUri: Uri? = null


    private val viewModel: HomeViewModel by viewModels {
        val dataTitle = resources.getStringArray(R.array.data_title)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)
        HomeViewModelFactory(dataTitle, dataDescription, dataPhoto)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupName()
        with(binding){
            btnGallery.setOnClickListener { startGallery() }
            btnCamera.setOnClickListener { startCamera() }
            btnScan.setOnClickListener { uploadImage() }
        }
        setRecyclerView()

        return root
    }

    private fun setupName() {
        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("user_name", null)
        if (!userName.isNullOrEmpty()) {
            binding.tvMainName.text = getString(R.string.main_welcome_msg, userName)
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val destinationUri = Uri.fromFile(createTempFile("image", ".jpeg"))
            UCrop.of(uri, destinationUri)
                .withAspectRatio(16f, 16f)
                .withMaxResultSize(1000, 1000)
                .start(requireActivity(), this)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(requireActivity())
        launcherIntentCamera.launch(currentImageUri)
    }
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            val destinationUri = Uri.fromFile(createTempFile("image", ".jpeg"))
            currentImageUri?.let {
                UCrop.of(it, destinationUri)
                    .withAspectRatio(16f, 16f)
                    .withMaxResultSize(1000, 1000)
                    .start(requireActivity(), this)
            }
        }
    }
    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivMainTrash.setImageURI(it)
        }
    }
    private fun uploadImage() {
        currentImageUri?.let {
            val intent = Intent(requireActivity(), ResultActivity::class.java)
            intent.putExtra(EXTRA_IMAGE_URI, currentImageUri.toString())
            startActivity(intent)
        }?: showToast(getString(R.string.empty_img_warning))
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri: Uri? = UCrop.getOutput(data!!)
            if (resultUri != null) {
                currentImageUri = resultUri
                showImage()
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError: Throwable? = UCrop.getError(data!!)
            Toast.makeText(requireActivity(), "Error during cropping: ${cropError?.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun setRecyclerView(){
        with(binding){
            rvArticle.setHasFixedSize(true)
            rvArticle.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            viewModel.articles.observe(viewLifecycleOwner) { articles ->
                adapter = ArticleAdapter(articles)
                rvArticle.adapter = adapter
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        currentImageUri = null
    }
    override fun onResume() {
        super.onResume()
        binding.rvArticle.scrollToPosition(0)
    }

    companion object {
        const val KEY_DETAIL = "key_detail"
        const val EXTRA_IMAGE_URI = "extra_image_uri"
    }
}
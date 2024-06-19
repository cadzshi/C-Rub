package com.bangkit.capstoneproject.cleanrubbish.ui.result

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bangkit.capstoneproject.cleanrubbish.R
import com.bangkit.capstoneproject.cleanrubbish.data.local.db.History
import com.bangkit.capstoneproject.cleanrubbish.data.local.repo.HistoryRepository
import com.bangkit.capstoneproject.cleanrubbish.data.remote.response.PredictResponse
import com.bangkit.capstoneproject.cleanrubbish.data.remote.retrofit.ApiConfig
import com.bangkit.capstoneproject.cleanrubbish.data.utils.reduceFileImage
import com.bangkit.capstoneproject.cleanrubbish.data.utils.uriToFile
import com.bangkit.capstoneproject.cleanrubbish.databinding.ActivityResultBinding
import com.bangkit.capstoneproject.cleanrubbish.helper.ViewModelFactory
import com.bumptech.glide.load.ImageHeaderParser.ImageType
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private lateinit var resultViewModel: ResultViewModel
    private lateinit var historyRepository: HistoryRepository

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        historyRepository = HistoryRepository(application)
        resultViewModel = obtainViewModel(this@ResultActivity)

        setTitle()

        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        imageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivResultScan.setImageURI(it)
        }
        showLoading(true)
        startClassify(imageUri)

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun startClassify(imageUri: Uri) {
        val imageFile = uriToFile(imageUri, this).reduceFileImage()

        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "image",
            imageFile.name,
            requestImageFile
        )

        lifecycleScope.launch {
            try {
                val apiService = ApiConfig.getApiService()
                val successResponse = apiService.uploadImage(multipartBody)

                val label = successResponse.data?.result
                if (!label.isNullOrEmpty()) {
                    binding.tvResult.text = label
                }
                when (label) {
                    "Organic" -> {
                        binding.ivTypeTrashCan.setImageResource(R.drawable.organic_trash_can)
                    }
                    else -> {
                        binding.ivTypeTrashCan.setImageResource(R.drawable.non_organic_trash_can)
                    }
                }

                val historyResult = label?.let { History(image = imageUri.toString(), label = it) }

                if (historyResult != null) {
                    resultViewModel.insertBookmarkResult(historyResult)
                }
            } catch (e: HttpException) {

                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, PredictResponse::class.java)
                showToast(errorResponse.message.toString())
                Log.e(TAG, "HTTP Exception: ${e.message()}")
            } catch (e: Exception) {
                showToast("An error occurred: ${e.message}")
                Log.e(TAG, "Exception: ${e.message}")
            } finally {
                showLoading(false)
            }
        }
    }

    private fun setTitle() {
        supportActionBar?.title = resources.getString(R.string.title_activity_result)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): ResultViewModel {
        historyRepository = HistoryRepository(application)
        val factory = ViewModelFactory.getInstance(historyRepository)
        return ViewModelProvider(activity, factory)[ResultViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbResult.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    companion object {
        const val TAG = "ResultActivity"
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val KEY_TYPE = "key_type"
    }
}

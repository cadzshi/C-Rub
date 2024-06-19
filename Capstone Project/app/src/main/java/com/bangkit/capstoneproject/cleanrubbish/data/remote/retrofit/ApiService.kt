package com.bangkit.capstoneproject.cleanrubbish.data.remote.retrofit

import com.bangkit.capstoneproject.cleanrubbish.data.remote.response.PredictResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("predict")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): PredictResponse
}
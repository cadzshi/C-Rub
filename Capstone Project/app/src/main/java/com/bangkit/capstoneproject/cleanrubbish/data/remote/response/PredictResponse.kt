package com.bangkit.capstoneproject.cleanrubbish.data.remote.response

import com.google.gson.annotations.SerializedName

data class PredictResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Data(

	@field:SerializedName("result")
	val result: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("confidenceScore")
	val confidenceScore: String? = null,

	@field:SerializedName("isAboveThreshold")
	val isAboveThreshold: Boolean? = null,

	@field:SerializedName("id")
	val id: String? = null
)

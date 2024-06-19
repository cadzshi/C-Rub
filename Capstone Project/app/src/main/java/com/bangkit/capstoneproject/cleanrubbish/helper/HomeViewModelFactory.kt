package com.bangkit.capstoneproject.cleanrubbish.helper

import android.content.res.TypedArray
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.capstoneproject.cleanrubbish.ui.home.HomeViewModel

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(private val dataTitle: Array<String>, private val dataDescription: Array<String>, private val dataPhoto: TypedArray) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(dataTitle, dataDescription, dataPhoto) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
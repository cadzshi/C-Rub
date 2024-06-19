package com.bangkit.capstoneproject.cleanrubbish.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.capstoneproject.cleanrubbish.data.local.db.History
import com.bangkit.capstoneproject.cleanrubbish.data.local.repo.HistoryRepository

import kotlinx.coroutines.launch

class ProfileViewModel(private val historyRepository: HistoryRepository): ViewModel() {


    fun deleteHistory(){
        viewModelScope.launch {
            try {
                historyRepository.delete()
            }catch (e: Exception){
                Log.d(TAG, "Gagal Menghapus Data: ${e.message}")
            }
        }
    }
    companion object {
        const val TAG = "ProfileViewModel"
    }
}
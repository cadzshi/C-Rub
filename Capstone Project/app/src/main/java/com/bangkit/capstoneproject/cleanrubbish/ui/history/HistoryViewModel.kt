package com.bangkit.capstoneproject.cleanrubbish.ui.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.capstoneproject.cleanrubbish.data.local.db.History
import com.bangkit.capstoneproject.cleanrubbish.data.local.repo.HistoryRepository
import kotlinx.coroutines.launch

class HistoryViewModel(private val historyRepository: HistoryRepository): ViewModel() {

    private val _historyResult = historyRepository.getAllHistoryResult()
    val history: LiveData<List<History>> = _historyResult

    init {
        getHistoryResult()
    }

    private fun getHistoryResult(){
        viewModelScope.launch {
            try {
                historyRepository.getAllHistoryResult()
            }catch (e: Exception){
                Log.d(TAG, "Gagal Mendapatkan Data: ${e.message}")
            }
        }
    }

    companion object {
        const val TAG = "HistoryViewModel"
    }

}
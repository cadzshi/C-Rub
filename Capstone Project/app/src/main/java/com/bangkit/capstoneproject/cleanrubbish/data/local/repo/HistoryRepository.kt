package com.bangkit.capstoneproject.cleanrubbish.data.local.repo

import android.app.Application
import androidx.lifecycle.LiveData
import com.bangkit.capstoneproject.cleanrubbish.data.local.db.History
import com.bangkit.capstoneproject.cleanrubbish.data.local.db.HistoryDao
import com.bangkit.capstoneproject.cleanrubbish.data.local.db.HistoryRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HistoryRepository (application: Application){

    private val mHistoryDao: HistoryDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db= HistoryRoomDatabase.getDatabase(application)
        mHistoryDao = db.historyDao()
    }

    fun insert(history: History){
        executorService.execute {
            mHistoryDao.insert(history)
        }
    }
    fun delete(){
        executorService.execute {
            mHistoryDao.deleteData()
        }
    }
    fun getAllHistoryResult(): LiveData<List<History>> = mHistoryDao.getAllHistoryResult()

}
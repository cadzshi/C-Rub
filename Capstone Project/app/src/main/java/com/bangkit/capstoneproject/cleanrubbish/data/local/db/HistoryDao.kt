package com.bangkit.capstoneproject.cleanrubbish.data.local.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(history: History)

    @Query("SELECT * from History")
    fun getAllHistoryResult(): LiveData<List<History>>

    @Query("DELETE from History")
    fun deleteData()

}
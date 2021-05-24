package com.example.mycats.cats.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CatDao {
    @Query("SELECT * FROM cat")
    fun getCats(): LiveData<List<Cat>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cat: List<Cat>)
}
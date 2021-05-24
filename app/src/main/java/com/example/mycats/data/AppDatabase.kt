package com.example.mycats.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mycats.cats.data.Cat
import com.example.mycats.cats.data.CatDao

@Database(entities = [Cat::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun catsDao(): CatDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                synchronized(AppDatabase::class.java) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                                context.applicationContext, AppDatabase::class.java, "cat"
                        ).build()
                    }
                }
            }
            return instance!!
        }
    }
}
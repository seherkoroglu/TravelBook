package com.seherkoroglu.kotlinmaps.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.seherkoroglu.kotlinmaps.model.Yer

@Database(entities = [Yer::class], version = 1)
abstract class YerDb : RoomDatabase() {
    abstract fun yerDao(): YerDao
}

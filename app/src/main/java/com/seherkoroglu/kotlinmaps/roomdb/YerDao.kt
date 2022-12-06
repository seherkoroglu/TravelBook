package com.seherkoroglu.kotlinmaps.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.seherkoroglu.kotlinmaps.model.Yer
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao //Data access object
interface YerDao {
    @Query("SELECT * FROM Yer")
    fun HepsiniAl() : Flowable<List<Yer>>

    @Insert
    fun insert(yer: Yer) : Completable
    //fun insert(place: Place)

    @Delete
    fun sil(yer: Yer) : Completable
    //fun delete(place: Place)
}
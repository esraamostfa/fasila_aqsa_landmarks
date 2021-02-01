package com.fasila.aqsalandmarks.model.card

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CardDao {

    @Insert
    suspend fun insertAll(cards: List<Card>)

    @Query("SELECT * FROM cards_table")
    fun getAll() : LiveData<List<Card>>

    @Query("SELECT COUNT(*) FROM cards_table")
    suspend fun count(): Int

    @Query("SELECT * FROM cards_table WHERE cardId = :key")
    fun getById(key: String) : Card
}
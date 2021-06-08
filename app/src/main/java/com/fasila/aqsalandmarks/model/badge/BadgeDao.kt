package com.fasila.aqsalandmarks.model.badge

import androidx.lifecycle.LiveData
import androidx.room.*
import com.fasila.aqsalandmarks.model.quiz.Quiz
import com.fasila.aqsalandmarks.model.stage.Stage

@Dao
interface BadgeDao {

    @Insert
    suspend fun insertAll(badges: List<Badge>)

    @Query("SELECT COUNT(*) FROM badges_table")
    suspend fun count(): Int

    @Query("SELECT * FROM badges_table WHERE id = :key")
    fun getById(key: String) : Badge

    @Query("SELECT * FROM badges_table")
    fun getAllBadges() : LiveData<List<Badge>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(badge: Badge)

    @Query("DELETE FROM badges_table")
    suspend fun deleteAll()
}
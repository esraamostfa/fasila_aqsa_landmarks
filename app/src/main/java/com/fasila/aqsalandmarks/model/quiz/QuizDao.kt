package com.fasila.aqsalandmarks.model.quiz

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.fasila.aqsalandmarks.model.card.Card

@Dao
interface QuizDao {

    @Insert
    suspend fun insertAll(quizzes: List<Quiz>)

    @Query("SELECT COUNT(*) FROM quizzes_table")
    suspend fun count(): Int

    @Query("SELECT * FROM quizzes_table WHERE id = :key")
    fun getById(key: String) : Quiz
}
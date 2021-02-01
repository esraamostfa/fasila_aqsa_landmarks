package com.fasila.aqsalandmarks.model.quiz

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.fasila.aqsalandmarks.model.converters.QuizzesAnswersConverter

@Entity(tableName = "quizzes_table")
data class Quiz(
    @PrimaryKey
    val id: String,
    val quizStageId: String,
    val question: String,
    @TypeConverters(QuizzesAnswersConverter::class)
    val answers: List<String>,
    val correctAnswer: String
)
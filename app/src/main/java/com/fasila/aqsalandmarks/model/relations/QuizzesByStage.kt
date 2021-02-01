package com.fasila.aqsalandmarks.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.fasila.aqsalandmarks.model.quiz.Quiz
import com.fasila.aqsalandmarks.model.stage.Stage

data class QuizzesByStage (
    @Embedded
    val stage: Stage,
    @Relation(parentColumn = "id", entityColumn = "quizStageId")
    val quizzes: List<Quiz>
    )
package com.fasila.aqsalandmarks.model.stage

import androidx.lifecycle.LiveData
import androidx.room.*
import com.fasila.aqsalandmarks.model.relations.QuizzesByStage

@Dao
interface StageDao {

    @Insert
    suspend fun insert(stage: Stage)

    @Update
    suspend fun update(stage: Stage)

    @Query("SELECT * FROM stage_table WHERE id = :key")
    fun get(key: String) : Stage

    @Query("SELECT * FROM stage_table")
    fun getAllStages() : LiveData<List<Stage>>

    @Query("DELETE FROM stage_table")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM stage_table")
    suspend fun count(): Int

    @Insert
    suspend fun insertAll(stages: List<Stage>)

    @Transaction
    @Query("SELECT * FROM STAGE_TABLE WHERE id = :stageId")
    fun getQuizzesByStage(stageId: String): QuizzesByStage
}
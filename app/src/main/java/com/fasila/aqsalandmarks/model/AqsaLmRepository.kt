package com.fasila.aqsalandmarks.model

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.fasila.aqsalandmarks.model.badge.Badge
import com.fasila.aqsalandmarks.model.badge.BadgeDao
import com.fasila.aqsalandmarks.model.card.Card
import com.fasila.aqsalandmarks.model.card.CardDao
import com.fasila.aqsalandmarks.model.profile.Profile
import com.fasila.aqsalandmarks.model.profile.ProfileDao
import com.fasila.aqsalandmarks.model.quiz.QuizDao
import com.fasila.aqsalandmarks.model.relations.QuizzesByStage
import com.fasila.aqsalandmarks.model.stage.Stage
import com.fasila.aqsalandmarks.model.stage.StageDao

class AqsaLmRepository(
    private val stageDao: StageDao,
    private val cardDao: CardDao,
    private val quizDao: QuizDao,
    private val profileDao: ProfileDao,
    private val badgeDao: BadgeDao
) {

    suspend fun updateStage(stage: Stage) = stageDao.update(stage)

    fun getStageById(id: String): Stage = stageDao.get(id)

    fun getAllStages(): LiveData<List<Stage>> = stageDao.getAllStages()

    fun getCardById(id: String): Card = cardDao.getById(id)

    fun getQuizzesByStage(stageId: String): QuizzesByStage = stageDao.getQuizzesByStage(stageId)

    fun insertProfile(profile: Profile) = profileDao.insert(profile)

    fun getProfileById(id: String): Profile = profileDao.get(id)

    fun deleteProfile(profile: Profile) = profileDao.delete(profile)

    fun getBadgeById(id: String) = badgeDao.getById(id)

    fun getAllBadges() : LiveData<List<Badge>> = badgeDao.getAllBadges()

    suspend fun updateBadge(badge: Badge) = badgeDao.update(badge)

}
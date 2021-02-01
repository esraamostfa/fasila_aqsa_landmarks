package com.fasila.aqsalandmarks.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fasila.aqsalandmarks.getJsonDataFromAsset
import com.fasila.aqsalandmarks.model.badge.Badge
import com.fasila.aqsalandmarks.model.badge.BadgeDao
import com.fasila.aqsalandmarks.model.card.Card
import com.fasila.aqsalandmarks.model.card.CardDao
import com.fasila.aqsalandmarks.model.converters.QuizzesAnswersConverter
import com.fasila.aqsalandmarks.model.converters.UserConverter
import com.fasila.aqsalandmarks.model.profile.Profile
import com.fasila.aqsalandmarks.model.profile.ProfileDao
import com.fasila.aqsalandmarks.model.quiz.Quiz
import com.fasila.aqsalandmarks.model.quiz.QuizDao
import com.fasila.aqsalandmarks.model.stage.Stage
import com.fasila.aqsalandmarks.model.stage.StageDao
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [Stage::class, Card::class, Quiz::class, Profile::class, Badge::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(QuizzesAnswersConverter::class, UserConverter::class)
abstract class AqsaLmarksDb : RoomDatabase() {
    abstract val stageDao: StageDao
    abstract val cardDao: CardDao
    abstract val quizDao: QuizDao
    abstract val profileDao: ProfileDao
    abstract val badgeDao: BadgeDao

    private val gson = Gson()

    private val _stages = MutableLiveData<List<Stage>>()
    val stages: LiveData<List<Stage>>
        get() = _stages

    fun populateStages(context: Context, scope: CoroutineScope) {

        val json = getJsonDataFromAsset(context, "stages.json")

        val listType = object : TypeToken<List<Stage>>() {}.type
        val list = gson.fromJson<List<Stage>>(json, listType)
        _stages.value = list

        scope.launch {
            if (stageDao.count() == 0) {
                _stages.value?.let { stages ->
                    stageDao.insertAll(stages)
                }
            }
        }
    }

    private val _cards = MutableLiveData<List<Card>>()
    val cards: LiveData<List<Card>>
        get() = _cards

    fun populateCards(context: Context, scope: CoroutineScope) {
        val json = getJsonDataFromAsset(context, "cards.json")

        val listType = object : TypeToken<List<Card>>() {}.type
        val list = gson.fromJson<List<Card>>(json, listType)
        _cards.value = list

        scope.launch {
            if (cardDao.count() == 0) {
                _cards.value?.let { cards ->
                    cardDao.insertAll(cards)
                }
            }
        }
    }

    private val _quizzes = MutableLiveData<List<Quiz>>()
    val quizzes: LiveData<List<Quiz>>
        get() = _quizzes

    fun populateQuizzes(context: Context, scope: CoroutineScope) {
        val json = getJsonDataFromAsset(context, "quizzes.json")

        val listType = object : TypeToken<List<Quiz>>() {}.type
        val list = gson.fromJson<List<Quiz>>(json, listType)
        _quizzes.value = list

        scope.launch {
            if (quizDao.count() == 0) {
                _quizzes.value?.let { quizzes ->
                    quizDao.insertAll(quizzes)
                }
            }
        }
    }

    private val _badges = MutableLiveData<List<Badge>>()
    val badges: LiveData<List<Badge>>
        get() = _badges

    fun populateBadges(context: Context, scope: CoroutineScope) {

        val json = getJsonDataFromAsset(context, "badges.json")

        val listType = object : TypeToken<List<Badge>>() {}.type
        val list = gson.fromJson<List<Badge>>(json, listType)
        _badges.value = list

        scope.launch {
            if (badgeDao.count() == 0) {
                _badges.value?.let { badges ->
                    badgeDao.insertAll(badges)
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AqsaLmarksDb? = null

        fun getInstance(context: Context, scope: CoroutineScope): AqsaLmarksDb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AqsaLmarksDb::class.java,
                        "aqsa_landmarks_database"
                    )
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                    instance.populateStages(context, scope)
                    instance.populateCards(context, scope)
                    instance.populateQuizzes(context, scope)
                    instance.populateBadges(context, scope)
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
package com.fasila.aqsalandmarks.model.profile

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.fasila.aqsalandmarks.model.stage.Stage
import java.util.*

@Entity(tableName = "profiles_table")
data class Profile(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    @TypeConverters
    val user: User? = null,
    //var passedStages: MutableList<String>,
    var hearts: Int = 5
)
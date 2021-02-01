package com.fasila.aqsalandmarks.model.stage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "stage_table")
data class Stage(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val number: String,
    @ColumnInfo(name = "name")
    val name: String,
    var passed: Boolean,
    var score: Int = 0,
    val badgeId: String
)
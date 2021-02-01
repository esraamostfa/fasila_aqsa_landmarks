package com.fasila.aqsalandmarks.model.badge

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "badges_table")
data class Badge(
    @PrimaryKey
    val id: String,
    val image: String,
    val name: String,
    val description: String,
    val goal: Int,
    var achieve: Int = 0
)

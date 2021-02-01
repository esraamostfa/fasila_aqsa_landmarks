package com.fasila.aqsalandmarks.model.card

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cards_table")
data class Card(
    @PrimaryKey(autoGenerate = false)
    val cardId: String,
    val image: String,
    val content: String
)

{
    val uri : String
    get() = "drawable/$image"
}
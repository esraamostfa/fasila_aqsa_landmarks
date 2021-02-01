package com.fasila.aqsalandmarks.model.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class QuizzesAnswersConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromAnswers(list: List<String>) : String = gson.toJson(list)

    @TypeConverter
    fun toAnswers(json: String) : List<String> {
        val  listType = object : TypeToken<List<String>>() {}.type

        return try{
            gson.fromJson(json, listType)
        } catch (error: Throwable) {
            emptyList()
        }
    }
}
package com.fasila.aqsalandmarks.model.converters

import androidx.room.TypeConverter
import com.fasila.aqsalandmarks.model.profile.User
import com.google.gson.Gson

class UserConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromUser(user: User) : String = gson.toJson(user)

    @TypeConverter
    fun toUser( stringUser: String) : User = gson.fromJson(stringUser,  User::class.java)
}
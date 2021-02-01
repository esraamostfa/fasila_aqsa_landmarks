package com.fasila.aqsalandmarks.model.profile

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProfileDao {

    @Insert
    fun insert(profile: Profile)

    @Query("SELECT * FROM profiles_table WHERE id = :key")
    fun get(key: String) : Profile

    @Delete
    fun delete(profile: Profile)
}
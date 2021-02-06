package com.fasila.aqsalandmarks.model.realtime

data class Stage (
    val id: String,
    var passed: Boolean,
    var score: Int = 0,
        )
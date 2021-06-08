package com.fasila.aqsalandmarks.model.realtime

data class Stage (
    val stageId: String= "",
    var passed: Boolean = false,
    var score: Int = 0,
        )
package com.fasila.aqsalandmarks.ui.stages

import com.fasila.aqsalandmarks.model.stage.Stage

class StageListener (val clickListener: (stage: Stage) -> Unit) {

    fun onClick (stage: Stage) = clickListener(stage)
}
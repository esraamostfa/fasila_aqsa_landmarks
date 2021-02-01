package com.fasila.aqsalandmarks.ui.stages

import com.fasila.aqsalandmarks.model.stage.Stage

class StageListener (val clickListener: (stageId: String) -> Unit) {

    fun onClick (stage: Stage) = clickListener(stage.id)
}
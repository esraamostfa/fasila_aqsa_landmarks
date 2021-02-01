package com.fasila.aqsalandmarks.ui.stages

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.fasila.aqsalandmarks.R
import com.fasila.aqsalandmarks.model.stage.Stage

@BindingAdapter("stageImage")
fun ImageView.setStageImage(item: Stage) {
    val headers = listOf(0, 16, 22, 30, 47, 61, 65, 75)
    val footers = listOf(15, 21, 29, 46, 60, 64, 74, 90, 95, 100)

    when (item.id.toInt()) {
        in headers -> setImageResource(R.drawable.rectangle)
        91, 96 -> setImageResource(R.drawable.rectangle_large)
        in footers -> setImageResource(if (item.passed) R.drawable.star else R.drawable.star_dark)
        else -> setImageResource(if (item.passed) R.drawable.circle else R.drawable.circle_dark)
    }
}


@BindingAdapter("stageName")
fun TextView.setStageName(item: Stage) { text = item.number }

@BindingAdapter("stageTextColor")
fun TextView.setStageTextColor(item: Stage) {
    val headers = listOf(0, 16, 22, 30, 47, 61, 65, 75, 91, 96)
    val color : Int = if (item.passed) R.color.TextColorPrimary else if (item.id.toInt() in headers ) R.color.backgroundColor else R.color.non_passed_stage_text_color
    setTextColor(resources.getColor(color))
}

@BindingAdapter("stageEnable")
fun ImageView.enable(item: Stage) {
    isEnabled = item.passed
}

@BindingAdapter("stageStar1")
fun ImageView.visiblity1(item: Stage) {
    if (item.score >= 1) visibility = View.VISIBLE else visibility = View.GONE
}

@BindingAdapter("stageStar2")
fun ImageView.visiblity2(item: Stage) {
    if (item.score >= 2) visibility = View.VISIBLE else visibility = View.GONE
}

@BindingAdapter("stageStar3")
fun ImageView.visiblity3(item: Stage) {
    if (item.score == 3) visibility = View.VISIBLE else visibility = View.GONE
}

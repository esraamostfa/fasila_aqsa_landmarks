package com.fasila.aqsalandmarks.ui.stages

import android.view.View
import android.widget.CalendarView
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
            101 -> setImageResource(R.drawable.circle_soon_header)
            in headers -> setImageResource(if (item.id.toInt() < 47) R.drawable.rectangle else R.drawable.rectangle_soon)
            91, 96 -> setImageResource(R.drawable.rectangle_large)
            in footers -> setImageResource(if (item.passed && item.score <= 2 && item.id.toInt() < 48) R.drawable.star_white else if (item.passed && item.score >= 2 && item.id.toInt() < 48) R.drawable.star else if (!item.passed && item.id.toInt() < 48) R.drawable.star_dark else R.drawable.star_soon)
            else -> setImageResource(if (item.passed && item.id.toInt() < 48)  R.drawable.circle else if (!item.passed && item.id.toInt() < 48) R.drawable.circle_dark else R.drawable.circle_soon)
        }
}


@BindingAdapter("stageName")
fun TextView.setStageName(item: Stage) { text = item.number }

@BindingAdapter("stageTextColor")
fun TextView.setStageTextColor(item: Stage) {
    val headers = listOf(0, 16, 22, 30, 47, 61, 65, 75, 91, 96, 101)
    val color : Int = if (item.passed) R.color.TextColorPrimary else if (item.id.toInt() in headers ) R.color.backgroundColor else R.color.non_passed_stage_text_color
    setTextColor(resources.getColor(color))
}

//@BindingAdapter("stageEnable")
//fun ImageView.enable(item: Stage) {
//    isEnabled = item.passed
//}

@BindingAdapter("stageStar1")
fun ImageView.visiblity1(item: Stage) {
    val footers = listOf(15, 21, 29, 46, 60, 64, 74, 90, 95, 100)
    visibility = if (item.score >= 1 && item.id.toInt() !in footers) View.VISIBLE else View.GONE
}

@BindingAdapter("stageStar2")
fun ImageView.visiblity2(item: Stage) {
    val footers = listOf(15, 21, 29, 46, 60, 64, 74, 90, 95, 100)
    visibility = if (item.score >= 2 && item.id.toInt() !in footers) View.VISIBLE else View.GONE
}

@BindingAdapter("stageStar3")
fun ImageView.visiblity3(item: Stage) {
    val footers = listOf(15, 21, 29, 46, 60, 64, 74, 90, 95, 100)
    visibility = if (item.score == 3 && item.id.toInt() !in footers) View.VISIBLE else View.GONE
}

@BindingAdapter("soonCoverVisibility")
fun androidx.cardview.widget.CardView.coverVisibility(item: Stage) {
    visibility = if (item.id.toInt() >= 48) View.VISIBLE else View.GONE
}

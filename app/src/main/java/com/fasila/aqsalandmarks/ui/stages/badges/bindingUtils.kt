package com.fasila.aqsalandmarks.ui.stages.badges

import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.fasila.aqsalandmarks.R
import com.fasila.aqsalandmarks.app.AqsaLandmarksApplication
import com.fasila.aqsalandmarks.model.badge.Badge
import com.fasila.aqsalandmarks.model.stage.Stage
import com.fasila.aqsalandmarks.ui.MainActivity

@BindingAdapter("badgeName")
fun TextView.setBadgeName(item: Badge) { text = item.name }

@BindingAdapter("badgeDescription")
fun TextView.setBadgeDescription(item: Badge) { text = item.description }

@BindingAdapter("badgeImage")
fun ImageView.setBadgeImage(item: Badge) {
    val imageResource = resources.getIdentifier(item.image, "drawable", AqsaLandmarksApplication.myPackageName)
    setImageResource(imageResource)
}

@BindingAdapter("badgeProgress")
fun ProgressBar.setProgress(item: Badge) {
    val badgeProgress = (item.achieve.toFloat()/item.goal*100).toInt()
    progress = badgeProgress
}

@BindingAdapter("badgeProgressText")
fun TextView.setProgressText(item: Badge) {
    val badgeProgress = (item.achieve.toFloat()/item.goal*100).toInt()
    text = "${badgeProgress.toString()} %"
}
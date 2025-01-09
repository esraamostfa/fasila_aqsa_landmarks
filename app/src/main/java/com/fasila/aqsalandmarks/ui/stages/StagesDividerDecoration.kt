package com.fasila.aqsalandmarks.ui.stages

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.fasila.aqsalandmarks.R

class StagesDividerDecoration (color: Int, private val heightInPixels: Int, private val context: Context) : RecyclerView.ItemDecoration() {

    private val paint = Paint()

    init {
        paint.color = color
        paint.isAntiAlias = true
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        val childCount = parent.childCount

        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val subStageImageBtn = child.findViewById<ImageView>(R.id.sub_stage_image_button)
            val isFooter = subStageImageBtn.drawable.constantState == ResourcesCompat.getDrawable(context.resources, R.drawable.star, null)?.constantState
                    || subStageImageBtn.drawable.constantState == ResourcesCompat.getDrawable(context.resources, R.drawable.star_dark, null)?.constantState
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom - params.bottomMargin*3
            val bottom = top + heightInPixels
            val left1 = parent.paddingLeft
            //val right1 = ((parent.width - parent.paddingRight)/2) - 121
            val right1 = parent.width/2 - parent.width/5.7
            val left2 = parent.width/2 + parent.width/5.7
            val right2 = parent.width - parent.paddingRight
            if (isFooter) {
                c.drawRect(left1.toFloat(), top.toFloat(), right1.toFloat(), bottom.toFloat(), paint)
                c.drawRect(left2.toFloat(), top.toFloat(), right2.toFloat(), bottom.toFloat(), paint)
            }
        }
    }

}

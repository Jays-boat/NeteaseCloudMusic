package com.jayboat.music.ui.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.ImageView
import com.jayboat.music.R
import com.jayboat.music.utils.DensityUtils

/**
 * Created by Hosigus on 2018/7/23.
 */
class PlayProgressControlButton(context: Context?, attrs: AttributeSet?) : ImageView(context, attrs) {

    private val progressPaint = Paint()
    private val rect = RectF(DensityUtils.dp2px(context, 2.2f), DensityUtils.dp2px(context, 2.2f), DensityUtils.dp2px(context, 29.8f), DensityUtils.dp2px(context, 29.8f))

    var progress = 0f
        set(value) {
            field=value
            invalidate()
        }
    var isPlaying = false
        private set


    init {
        progressPaint.style = Paint.Style.STROKE
        progressPaint.isAntiAlias = true
        progressPaint.color = Color.rgb(206, 61, 58)
        progressPaint.strokeWidth = DensityUtils.dp2px(context, 1.45f)

        this.setImageResource(R.drawable.ic_play_bottom)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (progress == 0f) {
            return
        }
        canvas?.drawArc(rect, -90f, 360 * progress, false, progressPaint)
    }

    fun switchPlayStatus() {
        isPlaying = !isPlaying
        if (isPlaying) {
            this.setImageResource(R.drawable.ic_play_bottom)
        } else {
            this.setImageResource(R.drawable.ic_pause_bottom)
        }
    }
}
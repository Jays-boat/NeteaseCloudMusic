package com.jayboat.music.ui.view

import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.ImageView
import com.jayboat.music.R
import com.jayboat.music.utils.DensityUtils

/**
 * Created by Hosigus on 2018/7/23.
 */
class PlayProgressControlButton(context: Context, attrs: AttributeSet?) : ImageView(context, attrs) {

    private val progressPaint = Paint()
    private val rect = RectF(DensityUtils.dp2px(context, 2.3f), DensityUtils.dp2px(context, 2.3f), DensityUtils.dp2px(context, 29.7f), DensityUtils.dp2px(context, 29.7f))
    private val pauseDrawable = ContextCompat.getDrawable(context, R.drawable.ic_pause_bottom)
    private val playDrawable = ContextCompat.getDrawable(context, R.drawable.ic_play_bottom)


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

        this.setImageDrawable(pauseDrawable)
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
            this.setImageDrawable(playDrawable)
        } else {
            this.setImageDrawable(pauseDrawable)
        }
    }
}
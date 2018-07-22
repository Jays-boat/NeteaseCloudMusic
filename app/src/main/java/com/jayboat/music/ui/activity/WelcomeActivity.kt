package com.jayboat.music.ui.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.jayboat.music.App
import com.jayboat.music.R
import com.jayboat.music.ui.activity.startMainActivity
import com.jayboat.music.utils.DensityUtils
import com.jayboat.music.utils.ThreadUtils
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {
    private var isThisActivityClosed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        DensityUtils.translucentStatusBar(window)

        var bgBitmap = BitmapFactory.decodeResource(resources, R.drawable.bg_welcome)
        val height = (DensityUtils.getScreenHeight(this).toDouble() / DensityUtils.getScreenWidth(this) * bgBitmap.width).toInt()
        bgBitmap = Bitmap.createBitmap(bgBitmap, 0, bgBitmap.height - height, bgBitmap.width, height)
        rl_welcome.background = BitmapDrawable(resources, bgBitmap)

        btn_skip.setOnClickListener {
            isThisActivityClosed = true
            onWelcomeDone()
        }

        ThreadUtils.getInstance().postDelay({
            if (!isThisActivityClosed) {
                onWelcomeDone()
            }
        },1000)
    }

    fun onWelcomeDone() {
        if (App.existUserInfo()) {
            startMainActivity(this)
        } else {
            startLoginActivity(this)
        }
        finish()
    }
}

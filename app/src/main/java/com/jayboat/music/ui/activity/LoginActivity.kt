package com.jayboat.music.ui.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.jayboat.music.App
import com.jayboat.music.R
import com.jayboat.music.utils.DensityUtils
import kotlinx.android.synthetic.main.activity_login.*

fun startLoginActivity(context: Context, cancelable: Boolean = true) {
    val intent = Intent(context, LoginActivity::class.java)
    intent.putExtra("cancelable", cancelable)
    context.startActivity(intent)
}

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        DensityUtils.translucentStatusBar(window)

        measureView()

        initListener()
    }

    override fun onResume() {
        super.onResume()
        if (App.isLoginSuccessful) {
            startMainActivity(this)
            finish()
        }
    }

    private fun measureView() {
        val screenWidth = DensityUtils.getScreenWidth(this)
        val screenHeight = DensityUtils.getScreenHeight(this)

        val layoutParams = iv_icon.layoutParams
        layoutParams.width = (0.25 * screenWidth).toInt()
        layoutParams.height = layoutParams.width
        iv_icon.layoutParams = layoutParams

        btn_login_phone.width = (0.8 * screenWidth).toInt()
        btn_login_phone.height = (0.0613 * screenHeight).toInt()

        btn_register.width = (0.8 * screenWidth).toInt()
        btn_register.height = (0.0613 * screenHeight).toInt()

        line_left.layoutParams.width = (screenWidth - tv_hint_bottom.width) / 2 - DensityUtils.dp2px(this, 60f)
        line_right.layoutParams.width = line_left.layoutParams.width

        ll_bottom.layoutParams.width = (0.825 * screenWidth).toInt()
    }

    private fun initListener() {
        if (intent.getBooleanExtra("cancelable", true)) {
            btn_cancel.setOnClickListener {
                startMainActivity(this@LoginActivity)
                finish()
            }
        } else {
            btn_cancel.visibility = View.GONE
        }
        btn_login_phone.setOnClickListener {
            startLoginPhoneActivity(this@LoginActivity)
        }
    }
}

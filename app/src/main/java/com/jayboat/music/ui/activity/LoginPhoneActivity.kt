package com.jayboat.music.ui.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.jayboat.music.App
import com.jayboat.music.R
import com.jayboat.music.bean.User
import com.jayboat.music.utils.DensityUtils
import com.jayboat.music.utils.NetUtils
import com.jayboat.music.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_login_phone.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun startLoginPhoneActivity(context: Context){
    val intent = Intent(context, LoginPhoneActivity::class.java)
    context.startActivity(intent)
}

class LoginPhoneActivity : AppCompatActivity() {

    private val mTag = "LoginPhoneActivity"

    private val loginPhoneCallback by lazy {
        object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.body()?.code == 200) {
                    App.loginSuccessfulByPhone(response.body(), et_phone.text.toString(), et_pwd.text.toString())
                    finish()
                } else {
                    ToastUtils.show("用户名或密码错误")
                    progressbar_login_phone.visibility = View.GONE
                    cover.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                ToastUtils.show("网络错误")
                Log.v(mTag,t.toString())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_phone)

        DensityUtils.translucentStatusBar(window)
        toolbar.setNavigationOnClickListener { finish() }

        cover.setOnTouchListener { _, _ -> true }
        btn_login.setOnClickListener {
            progressbar_login_phone.visibility = View.VISIBLE
            cover.visibility = View.VISIBLE
            NetUtils.loginByPhone(et_phone.text.toString(), et_pwd.text.toString(), loginPhoneCallback)
        }
    }

}

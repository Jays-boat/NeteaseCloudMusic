package com.jayboat.music.ui.fragment

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
import com.jayboat.music.R

/**
 * Created by Hosigus on 2018/7/18.
 */
class TempFragment : BaseFragment() {
    private var tempImageResId: Int = R.drawable.bg_login
    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        tempImageResId = if (args == null || args.getInt("tempImageResId") == 0) {
            R.drawable.bg_login
        } else {
            args.getInt("tempImageResId")
        }
    }

    fun setTempImageResId(tempImageResId:Int):TempFragment{
        val bundle = Bundle()
        bundle.putInt("tempImageResId", tempImageResId)
        arguments = bundle
        return this
    }

    override fun initView(v: View) {
        v.findViewById<ImageView>(R.id.iv_holder).setImageDrawable(ContextCompat.getDrawable(context!!,tempImageResId))
    }
}
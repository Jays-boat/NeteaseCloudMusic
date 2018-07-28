package com.jayboat.music.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jayboat.music.R

/**
 * Created by Hosigus on 2018/7/18.
 */
abstract class BaseFragment : Fragment() {
    var title: String? = null
    private var resId: Int = R.layout.layout_place_holder
    override fun setArguments(args: Bundle?) {
        super.setArguments(args)

        title = args?.getString("title")
        resId = if (args == null || args.getInt("resId") == 0) {
            R.layout.layout_place_holder
        } else {
            args.getInt("resId")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(getResId(), container, false)
        initView(v)
        return v
    }

    protected abstract fun initView(v:View)

    open protected fun getResId() = resId
}
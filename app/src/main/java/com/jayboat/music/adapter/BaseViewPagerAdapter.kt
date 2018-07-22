package com.jayboat.music.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.jayboat.music.ui.fragment.BaseFragment

/**
 * Created by Hosigus on 2018/7/18.
 */
open class BaseViewPagerAdapter<T : BaseFragment>(fm: FragmentManager, private val fragmentList:MutableList<T>):FragmentStatePagerAdapter(fm){
    override fun getPageTitle(position: Int): CharSequence? = fragmentList[position].title

    override fun getItem(position: Int): Fragment = fragmentList[position]

    override fun getCount(): Int = fragmentList.size
}

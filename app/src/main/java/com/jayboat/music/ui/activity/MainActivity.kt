package com.jayboat.music.ui.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Gravity
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.jayboat.music.App
import com.jayboat.music.R
import com.jayboat.music.adapter.BaseViewPagerAdapter
import com.jayboat.music.bean.TempMusic
import com.jayboat.music.ui.fragment.BaseFragment
import com.jayboat.music.ui.fragment.TempFragment
import com.jayboat.music.ui.view.BottomMusicBar
import com.jayboat.music.utils.DensityUtils
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_main.*
import kotlinx.android.synthetic.main.include_main_drawer_start.*

fun startMainActivity(context: Context) {
    val intent = Intent(context, MainActivity::class.java)
    context.startActivity(intent)
}

class MainActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DensityUtils.translucentStatusBar(window)

        initDrawerStartHeaderView()

        initToolbar()

        initBottomBar()
    }

    private fun initBottomBar() {
        bmb_main.setOnPlayControlCallback(object : BottomMusicBar.PlayControlCallback {
            override fun onPlay(progress: Float) {
                // TODO onPlayPress
            }
            override fun onPause(progress: Float) {
                // TODO onPausePress
            }

            override fun onMusicChange(position: Int) {
                // TODO onMusicSelect
            }
        })
        // TODO init MusicList
        bmb_main.setMusicList(listOf(
                TempMusic("生命線", "れをる", "http://p1.music.126.net/-FTQh54lp6TTe4PWgJC4PQ==/3288639278763632.jpg"),
                TempMusic("水底游歩道", "れをる", "http://p1.music.126.net/-FTQh54lp6TTe4PWgJC4PQ==/3288639278763632.jpg"),
                TempMusic("ハルシアン", "れをる", "http://p1.music.126.net/-FTQh54lp6TTe4PWgJC4PQ==/3288639278763632.jpg")))
        bmb_main.setProgress(0.7f)
    }

    // TODO init fragment
    private fun initPage() = mutableListOf<BaseFragment>(
            TempFragment(),
            TempFragment().setTempImageResId(R.drawable.temp_bg_main_discover),
            TempFragment().setTempImageResId(R.drawable.temp_bg_main_video))

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        toolbar.setNavigationOnClickListener {
            dl_main.openDrawer(Gravity.START)
        }

        vp_main.adapter = BaseViewPagerAdapter<BaseFragment>(supportFragmentManager, initPage())
        tl_main_toolbar.setupWithViewPager(vp_main)
        tl_main_toolbar.setSelectedTabIndicatorHeight(0)
        tl_main_toolbar.addOnTabSelectedListener(this)

        tl_main_toolbar.getTabAt(0)!!.customView = createTabIconView(R.drawable.ic_toolbar_main_music_normal)
        tl_main_toolbar.getTabAt(1)!!.customView = createTabIconView(R.drawable.ic_toolbar_main_discover_normal)
        tl_main_toolbar.getTabAt(2)!!.customView = createTabIconView(R.drawable.ic_toolbar_main_video_normal)
        tl_main_toolbar.getTabAt(1)!!.select()
    }

    private fun initDrawerStartHeaderView() {
        if (App.existUserInfo()) {
            val headerView = nv_main_start.inflateHeaderView(R.layout.layout_logined_header_main_drawer)
            val manager = Glide.with(this)
            manager.load(App.getUser().profile.avatarUrl).into(headerView.findViewById<CircleImageView>(R.id.civ_head))
            manager.load(App.getUser().profile.backgroundUrl).into(headerView.findViewById<ImageView>(R.id.iv_background))
            headerView.findViewById<TextView>(R.id.tv_name).text = App.getUser().profile.nickname
        } else {
            val headerView = nv_main_start.inflateHeaderView(R.layout.layout_unknown_header_main_drawer)
            headerView.findViewById<TextView>(R.id.btn_login).setOnClickListener{
                startLoginActivity(this@MainActivity, false)
            }
        }

        val toggle = ActionBarDrawerToggle(this, dl_main, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        dl_main.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun createTabIconView(imgResId: Int): ImageView {
        val ivIcon = ImageView(this)
        ivIcon.setImageResource(imgResId)
        return ivIcon
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {
        when (tab.position) {
            0 -> (tab.customView as ImageView).setImageResource(R.drawable.ic_toolbar_main_music_normal)
            1 -> (tab.customView as ImageView).setImageResource(R.drawable.ic_toolbar_main_discover_normal)
            2 -> (tab.customView as ImageView).setImageResource(R.drawable.ic_toolbar_main_video_normal)
        }
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        when (tab.position) {
            0 -> (tab.customView as ImageView).setImageResource(R.drawable.ic_toolbar_main_music_selected)
            1 -> (tab.customView as ImageView).setImageResource(R.drawable.ic_toolbar_main_discover_selected)
            2 -> (tab.customView as ImageView).setImageResource(R.drawable.ic_toolbar_main_video_selected)
        }
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {}
}

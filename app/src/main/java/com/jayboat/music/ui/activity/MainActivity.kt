package com.jayboat.music.ui.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
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
import com.jayboat.music.service.MusicPlayerService
import com.jayboat.music.ui.fragment.AlbumListFragment
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

    private val tempList = mutableListOf<TempMusic>(
            TempMusic("生命線", "れをる",
                    "http://p1.music.126.net/-FTQh54lp6TTe4PWgJC4PQ==/3288639278763632.jpg",
                    "http://music.163.com/song/media/outer/url?id=28315997.mp3"),
            TempMusic("水底游歩道", "れをる",
                    "http://p1.music.126.net/-FTQh54lp6TTe4PWgJC4PQ==/3288639278763632.jpg",
                    "http://music.163.com/song/media/outer/url?id=33516491.mp3"),
            TempMusic("ハルシアン", "れをる",
                    "http://p1.music.126.net/-FTQh54lp6TTe4PWgJC4PQ==/3288639278763632.jpg",
                    "http://music.163.com/song/media/outer/url?id=33516492.mp3"))

    private val serviceConnection = object :ServiceConnection{
        override fun onServiceDisconnected(name: ComponentName?) {
            musicControlBinder=null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            musicControlBinder = service as MusicPlayerService.MusicControlBinder
            initBottomBar()
        }

    }
    private var musicControlBinder: MusicPlayerService.MusicControlBinder? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindService()

        DensityUtils.translucentStatusBar(window)

        initDrawerStartHeaderView()

        initToolbar()
    }

    override fun onDestroy() {
        unbindService()
        super.onDestroy()
    }

    private fun bindService() {
        startService(Intent(this, MusicPlayerService::class.java))
        bindService(Intent(this, MusicPlayerService::class.java), serviceConnection, BIND_AUTO_CREATE)
    }

    private fun unbindService() {
        unbindService(serviceConnection)
    }

    private fun initBottomBar() {
        bmb_main.setOnPlayControlCallback(object : BottomMusicBar.PlayControlCallback {
            override fun play() {
                musicControlBinder?.play()
            }

            override fun pause() {
                musicControlBinder?.pause()
            }

            override fun changeMusic(pos: Int) {
                musicControlBinder?.playMusic(pos)
            }
        })
        // TODO init MusicList
        musicControlBinder?.setOnProgressUpdateListener {
            bmb_main.setProgress(it)
        }
        musicControlBinder?.setMusicList(tempList)
        bmb_main.setMusicList(tempList)
        bmb_main.setProgress(0.7f)
    }

    // TODO init fragment
    private fun initPage() = mutableListOf<BaseFragment>(
            AlbumListFragment(),
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

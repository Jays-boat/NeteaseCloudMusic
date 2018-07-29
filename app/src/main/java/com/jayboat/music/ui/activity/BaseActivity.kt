package com.jayboat.music.ui.activity

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.support.v4.view.PagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jayboat.music.R
import com.jayboat.music.service.MusicPlayerService
import com.jayboat.music.ui.view.BottomMusicBar
import com.makeramen.roundedimageview.RoundedImageView

/**
 * Created by Hosigus on 2018/7/28.
 */
@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity(),MusicPlayerService.MusicPlayerListener {
    protected var musicControlBinder: MusicPlayerService.MusicControlBinder? = null

    private var bmb: BottomMusicBar? = null
    private var musicCount = 0

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            musicControlBinder?.removeListener(this@BaseActivity)
            musicControlBinder=null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            musicControlBinder = service as MusicPlayerService.MusicControlBinder
            musicControlBinder?.addListener(this@BaseActivity)
            onServiceConnected()
        }
    }

    override fun onStart() {
        super.onStart()
        musicControlBinder ?: bindService()
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

    override fun onProgressUpdate(progress: Float) {
        bmb?.setProgress(progress)
    }

    override fun onMusicListChange() {
        musicCount = musicControlBinder?.getMusicList()?.trackCount ?: 0
        if (musicCount == 0) {
            bmb?.visibility = View.GONE
        } else {
            bmb?.visibility = View.VISIBLE
        }
        bmb?.refresh()
    }

    override fun onMusicSelect(pos: Int) {
        bmb?.chooseMusic(pos)
    }

    override fun onMusicStart() {
        bmb?.setPlayStatus(true)
    }

    override fun onMusicPause() {
        bmb?.setPlayStatus(false)
    }
    override fun onMusicStop() {
        bmb?.setPlayStatus(false)
        bmb?.setProgress(0f)
    }

    protected open fun onServiceConnected() {
        bmb = findViewById<BottomMusicBar>(R.id.bottom_music_bar) ?: return
        bmb!!.setAdapter(MusicPageAdapter())
        bmb!!.setOnPlayControlCallback(object : BottomMusicBar.PlayControlCallback {
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
        onMusicListChange()
    }

    internal inner class MusicPageAdapter : PagerAdapter() {
        override fun getCount(): Int {
            return musicCount
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun getItemPosition(`object`: Any): Int {
            return PagerAdapter.POSITION_NONE
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val v = LayoutInflater.from(this@BaseActivity).inflate(R.layout.item_info_bottom_music_bar, container, false)
            val track = musicControlBinder!!.getMusicList()!!.tracks[position]
            Glide.with(this@BaseActivity)
                    .load(track.album.blurPicUrl)
                    .apply(RequestOptions().placeholder(R.drawable.ic_default_bottom_music_icon))
                    .into(v.findViewById<View>(R.id.iv_music_icon_bottom) as RoundedImageView)
            val musicName = v.findViewById<TextView>(R.id.tv_music_name)
            val musicSingerName = v.findViewById<TextView>(R.id.tv_music_singer_name)
            musicName.text = track.name
            musicSingerName.text = track.artists[0].name
            container.addView(v)
            return v
        }
    }
}
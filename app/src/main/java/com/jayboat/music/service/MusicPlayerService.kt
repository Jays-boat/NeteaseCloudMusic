package com.jayboat.music.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.widget.MediaController
import com.jayboat.music.App
import com.jayboat.music.bean.TempMusic
import com.jayboat.music.utils.MyMediaPlayer
import com.jayboat.music.utils.ThreadUtils
import java.io.IOException

/**
 * Created by Hosigus on 2018/7/27.
 */
class MusicPlayerService : Service() {

    private var playingMusicIndicator = 0

    private var mMusicList = mutableListOf<TempMusic>()

    private val mMediaPlayer by lazy { MyMediaPlayer() }

    private val mMusicControlBinder by lazy { MusicControlBinder()  }

    override fun onBind(intent: Intent?): IBinder {
        return mMusicControlBinder
    }

    override fun onDestroy() {
        mMediaPlayer.release()
        super.onDestroy()
    }

    inner class MusicControlBinder : Binder() {

        fun pause() {
            mMediaPlayer.pause()
        }

        fun play() {
            if (mMusicList.isEmpty()) {
                return
            }
            if (mMediaPlayer.isPause()) {
                mMediaPlayer.start()
            } else {
                playMusic(playingMusicIndicator)
            }
        }

        fun stop() {
            mMediaPlayer.stop()
        }

        fun setMusicList(musicList: MutableList<TempMusic>) {
            mMusicList = musicList
            mMediaPlayer.reset()
            playingMusicIndicator = 0
        }

        fun playMusic(pos: Int) {
            if (pos < mMusicList.size) {
                playingMusicIndicator = pos
                try {
                    mMediaPlayer.reset()
                    mMediaPlayer.setDataSource(App.getProxy().getProxyUrl(mMusicList[pos].getMusicUrl()))
                    mMediaPlayer.prepare()
                    mMediaPlayer.seekTo(0)
                    mMediaPlayer.start()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        fun setOnProgressUpdateListener(listener:(progress: Float) -> Unit){
            mMediaPlayer.onProgressUpdateListener = {
                listener.invoke(it.toFloat() / mMediaPlayer.duration)
            }
        }
        fun setOnStartListener(listener: () -> Unit) {
            mMediaPlayer.onStartListener = listener
        }
        fun setOnPauseListener(listener: () -> Unit) {
            mMediaPlayer.onPauseListener = listener
        }
        fun setOnStopListener(listener: () -> Unit) {
            mMediaPlayer.onStopListener = listener
        }
    }
}
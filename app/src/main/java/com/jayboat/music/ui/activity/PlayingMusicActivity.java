package com.jayboat.music.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.commit451.nativestackblur.NativeStackBlur;
import com.jayboat.music.App;
import com.jayboat.music.R;
import com.jayboat.music.bean.SongList;
import com.jayboat.music.service.MusicPlayerService;
import com.jayboat.music.ui.view.DiskImageView;
import com.jayboat.music.utils.DensityUtils;
import com.jayboat.music.utils.ThreadUtils;
import com.jayboat.music.utils.ToastUtils;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class PlayingMusicActivity extends BaseActivity implements View.OnClickListener {
    public static void actionStart(Context context, SongList mMusic, int pos) {
        Intent intent = new Intent(context, PlayingMusicActivity.class);
        intent.putExtra("music", mMusic);
        intent.putExtra("position", pos);
        context.startActivity(intent);
    }

    private ConstraintLayout mCtl;
    private FrameLayout mFl;

    private TextView title;
    private TextView nowTime;
    private TextView totalTime;
    private ImageView cycleMode;
    private ImageView musicStatus;
    private SeekBar mSeekBar;
    private DiskImageView mDisk;

    private List<SongList.ResultBean.TracksBean> mSongs;
    private List<SongList.ResultBean.TracksBean> originSongs;
    private MusicPlayerService.MusicControlBinder musicControlBinder;

    private int cycleOrder = 0;
    private int cycleStatus;
    private int position;
    private boolean bind;
    private boolean status;


    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        musicControlBinder = getMusicControlBinder();
        bind = true;
        initMusicData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_music);
        Intent intent = getIntent();
        SongList mSong = (SongList) intent.getSerializableExtra("music");
        position = intent.getIntExtra("position", -1);
        DensityUtils.translucentStatusBar(getWindow());

        initLocalView();

        boolean checkData = mSong == null || position == -1;
        if (checkData) {
            ToastUtils.show("未获取歌曲信息:(");
            finish();
        } else {
            mSongs = mSong.getResult().getTracks();
            originSongs = mSongs;
            getData(mSongs.get(position));
            status = true;
        }

    }

    private void getData(SongList.ResultBean.TracksBean mMusic) {
        Glide.with(App.getAppContext())
                .asDrawable()
                .load(mMusic.getAlbum().getBlurPicUrl())
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        mDisk.setUpPicture(resource);
                        Bitmap temp = ((BitmapDrawable) resource).getBitmap();
                        Bitmap blur = NativeStackBlur.process(temp, 100);
                        Drawable drawable = new BitmapDrawable(getResources(), blur);
                        drawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                        mCtl.setBackground(drawable);
                        }
                });
        StringBuilder info = new StringBuilder();
        info.append(mMusic.getName());
        info.append(" - ");
        for (int i = 0; i < mMusic.getArtists().size(); i++) {
            info.append(mMusic.getArtists().get(i).getName());
            info.append("/");
        }
        info = new StringBuilder(info.substring(0, info.length() - 1));
        title.setText(info);
        mDisk.stopDisk();
        mDisk.playDisk();
    }

    @Override
    public void onProgressUpdate(float progress) {
        super.onProgressUpdate(progress);
        int pos = (int) (progress * musicControlBinder.getDuration());
        mSeekBar.setProgress(pos);
        nowTime.setText(formatTime(pos));
        if ((int)progress == 1) {
            position = position + 1;
            musicControlBinder.playMusic(position);
            getData(mSongs.get(position));
            initMusicData();
        }
    }

    private void initMusicData() {
        totalTime.setText(formatTime(musicControlBinder.getDuration()));
        mSeekBar.setMax(musicControlBinder.getDuration());
    }

    private void initLocalView() {
        mCtl = findViewById(R.id.ctl_playing);
        mFl = findViewById(R.id.fl_playing_container);
        addContainer(R.layout.container_playing_disc);
        mDisk = findViewById(R.id.div_playing);

        title = findViewById(R.id.tv_playing_song_name);
        nowTime = findViewById(R.id.tv_playing_time_now);
        totalTime = findViewById(R.id.tv_playing_time_total);
        cycleMode = findViewById(R.id.iv_playing_cycle_mode);
        musicStatus = findViewById(R.id.iv_playing_status);
        mSeekBar = findViewById(R.id.sb_playing);
        ImageView previousSong = findViewById(R.id.iv_playing_previous);
        ImageView nextSong = findViewById(R.id.iv_playing_next);
        ImageView getSongList = findViewById(R.id.iv_playing_show_list);
        ImageView back = findViewById(R.id.iv_playing_back);

        back.setOnClickListener(this);
        cycleMode.setOnClickListener(this);
        previousSong.setOnClickListener(this);
        nextSong.setOnClickListener(this);
        musicStatus.setOnClickListener(this);
        getSongList.setOnClickListener(this);

        cycleStatus = cycleOrder;
    }

    private void addContainer(@LayoutRes int resId) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(resId, null);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mFl.addView(contentView, params);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_playing_back:
                finish();
                break;
            case R.id.iv_playing_cycle_mode:
                cycleStatus = (cycleStatus + 1) % 3;
                int cycleRandom = 1;
                if (cycleStatus == cycleOrder) {
                    cycleMode.setImageDrawable(getResources().getDrawable(R.drawable.playing_press_cycle_order));
                    mSongs = originSongs;
                } else if (cycleStatus == cycleRandom) {
                    cycleMode.setImageDrawable(getResources().getDrawable(R.drawable.playing_press_cycle_random));
                    ToastUtils.show(getResources().getString(R.string.not_exploit));
                } else {
                    cycleMode.setImageDrawable(getResources().getDrawable(R.drawable.playing_press_cycle_only));
                    ToastUtils.show(getResources().getString(R.string.not_exploit));
                }
                break;
            case R.id.iv_playing_next:
                if (bind) {
                    position = position + 1;
                    musicControlBinder.playMusic(position);
                    getData(mSongs.get(position));
                    initMusicData();
                }
                break;
            case R.id.iv_playing_previous:
                if (bind) {
                    position = position - 1;
                    getData(mSongs.get(position));
                    musicControlBinder.playMusic(position);
                    initMusicData();
                }
                break;
            case R.id.iv_playing_status:
                if (bind) {
                    if (status) {
                        status = false;
                        musicControlBinder.pause();
                        mDisk.playDisk();
                        musicStatus.setImageDrawable(getResources().getDrawable(R.drawable.playing_press_continue));
                    } else {
                        status = true;
                        musicControlBinder.play();
                        mDisk.playDisk();
                        musicStatus.setImageDrawable(getResources().getDrawable(R.drawable.playing_press_pause));
                    }
                }
                break;
            case R.id.iv_playing_show_list:
            case R.id.iv_playing_like:
            case R.id.iv_playing_download:
            case R.id.iv_playing_more:
            case R.id.iv_playing_comment:
                ToastUtils.show(getResources().getString(R.string.not_exploit));
                break;
            default:
                break;
        }
    }

    private String formatTime(int length) {
        Date date = new Date(length);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        return simpleDateFormat.format(date);
    }
}
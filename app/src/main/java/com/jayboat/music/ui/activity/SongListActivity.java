package com.jayboat.music.ui.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.jayboat.music.App;
import com.jayboat.music.R;
import com.jayboat.music.adapter.SongListAdapter;
import com.jayboat.music.bean.SongList;
import com.jayboat.music.utils.DensityUtils;
import com.jayboat.music.utils.NetUtils;
import com.jayboat.music.utils.ToastUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongListActivity extends AppCompatActivity implements View.OnClickListener {

    private long albumID;
    private TextView title;
    private TextView albumName;
    private TextView creatorName;
    private CircleImageView creatorAvatar;
    private RoundedImageView albumImage;
    private RecyclerView mRv;
    private RelativeLayout mRl;
    private Toolbar mTb;
    private Bitmap backgroundBitmap;
    private SongList mSongList;
    private List<SongList.ResultBean.TracksBean> mTrackList;

    private static final String TAG = "SongListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);
        albumID = getIntent().getLongExtra("SongListId",-1);
        initLocalView();

        if (albumID == -1){
            ToastUtils.show("未获取到歌单具体内容:( 请重试");
            finish();
        } else {
            getData();
        }

    }

    private void getData(){
        NetUtils.getMusicList(albumID, new Callback<SongList>() {
            @Override
            public void onResponse(@NonNull Call<SongList> call, @NonNull Response<SongList> response) {
                if (response.body() != null){
                    mSongList = response.body();
                    mTrackList = response.body().getResult().getTracks();
                } else {
                    ToastUtils.show("内容获取有误，请重试:(");
                    Log.v(TAG,response.errorBody()+"");
                }
                initData();
            }

            @Override
            public void onFailure(@NonNull Call<SongList> call, @NonNull Throwable t) {
                ToastUtils.show("获取过程可能有点问题:(");
                Log.v(TAG,t.toString());
            }
        });
    }

    private void initData(){
        title.setText("歌单");

        SongList.ResultBean.CreatorBean creator = mSongList.getResult().getCreator();
        creatorName.setText(creator.getNickname());
        albumName.setText(mSongList.getResult().getName());
        Glide.with(App.getAppContext())
                .load(creator.getAvatarUrl())
                .into(creatorAvatar);
        Glide.with(App.getAppContext())
                .load(mSongList.getResult().getCoverImgUrl())
                .into(albumImage);
        Glide.with(App.getAppContext())
                .asBitmap()
                .load(mSongList.getResult().getCoverImgUrl())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        backgroundBitmap = resource;
                        Palette.from(backgroundBitmap).generate(palette -> {
                            DensityUtils.setStatusBarColor(getWindow(),palette.getDarkMutedColor(Color.parseColor("#000000")));
                            mRl.setBackgroundColor(palette.getDarkMutedColor(Color.parseColor("#000000")));
                            mTb.setBackgroundColor(palette.getDarkMutedColor(Color.parseColor("#000000")));
                        });
                    }
                });

        SongListAdapter songListAdapter = new SongListAdapter(mTrackList);
        mRv.setAdapter(songListAdapter);
        mRv.setLayoutManager(new LinearLayoutManager(App.getAppContext()));
        }

    private void initLocalView(){
        title = findViewById(R.id.tv_song_list_title);
        albumName = findViewById(R.id.tv_song_list_album_name);
        creatorName = findViewById(R.id.tv_song_list_creator);
        creatorAvatar = findViewById(R.id.civ_song_list_creator);
        albumImage = findViewById(R.id.riv_song_list_album);
        mRv = findViewById(R.id.rv_song_list);
        mRl = findViewById(R.id.rl_song_list);
        mTb = findViewById(R.id.tb_song_list);

        ImageView back = findViewById(R.id.iv_song_list_back);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_song_list_back:
                finish();
                break;
        }
    }
}

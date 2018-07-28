package com.jayboat.music.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jayboat.music.App;
import com.jayboat.music.R;
import com.jayboat.music.bean.SongList;
import com.jayboat.music.viewholder.SongListHeadViewHolder;
import com.jayboat.music.viewholder.SongListItemViewHolder;

import java.util.List;

public class SongListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEAD = -1;
    private static final int TYPE_ITEM = -2;
    private List<SongList.ResultBean.TracksBean> mMusicList;

    public SongListAdapter(List<SongList.ResultBean.TracksBean> list){
        this.mMusicList = list;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEAD : TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(App.getAppContext());
        if (viewType == TYPE_HEAD){
            View view = layoutInflater.inflate(R.layout.song_list_rv_item_head,parent,false);
            return new SongListHeadViewHolder(view,mMusicList.size());
        } else {
            View view = layoutInflater.inflate(R.layout.song_list_rv_item_song,parent,false);
            return new SongListItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SongListItemViewHolder){
            SongListItemViewHolder listItemViewHolder = (SongListItemViewHolder)holder;
            listItemViewHolder.initData(mMusicList.get(position - 1),position);
        }
    }

    @Override
    public int getItemCount() {
        return mMusicList.size() + 1;
    }
}

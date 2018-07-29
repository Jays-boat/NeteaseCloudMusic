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

import retrofit2.http.HEAD;

public class SongListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEAD = -1;
    private static final int TYPE_ITEM = -2;
    private List<SongList.ResultBean.TracksBean> mMusicList;
    private SongChooseListener listener;

    public SongListAdapter(List<SongList.ResultBean.TracksBean> list, SongChooseListener listener) {
        this.mMusicList = list;
        this.listener = listener;
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
        int type = getItemViewType(position);
        if (type == TYPE_ITEM) {
            ((SongListItemViewHolder) holder).initData(mMusicList.get(position - 1), position);
        }
        holder.itemView.setOnClickListener(v-> listener.onChoose(type == TYPE_HEAD ? 0 : position - 1));
    }

    @Override
    public int getItemCount() {
        return mMusicList.size() + 1;
    }

    public interface SongChooseListener{
        void onChoose(int pos);
    }
}

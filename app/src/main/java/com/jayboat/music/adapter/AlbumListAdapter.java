package com.jayboat.music.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.bumptech.glide.Glide;
import com.jayboat.music.viewholder.AlbumListFoldHolder;
import com.jayboat.music.viewholder.AlbumListNormalHolder;
import com.jayboat.music.viewholder.AlbumListSongHolder;
import com.jayboat.music.R;
import com.jayboat.music.bean.PlaylistBean;

import java.util.ArrayList;

public class AlbumListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private String[] text = {"我创建的歌单", "我收藏的歌单"};
    private ArrayList<PlaylistBean> myList;
    private ArrayList<PlaylistBean> collectList;

    public AlbumListAdapter(Context context, ArrayList<PlaylistBean> myList, ArrayList<PlaylistBean> collectList) {
        this.myList = myList;
        this.collectList = collectList;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return 3;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        switch (groupPosition) {
            case 0:
                return 0;
            case 1:
                return myList.size();
            case 2:
                return collectList.size();
            default:
                return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupPosition;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        AlbumListNormalHolder albumListNormalHolder;
        AlbumListFoldHolder albumListFoldHolder = null;
        if (convertView == null) {
            if (groupPosition == 0) {
                convertView = LayoutInflater.from(context).inflate(R.layout.album_list_rv_item_normal, null);
                albumListNormalHolder = new AlbumListNormalHolder();
                albumListNormalHolder.src = convertView.findViewById(R.id.iv_albumList_icon);
                albumListNormalHolder.word = convertView.findViewById(R.id.tv_albumList_word);
                convertView.setTag( albumListNormalHolder);
            } else {
                convertView = LayoutInflater.from(context).inflate(R.layout.album_list_rv_item_fold, null);
                albumListFoldHolder = new AlbumListFoldHolder();
                albumListFoldHolder.arrow = convertView.findViewById(R.id.iv_albumList_arrow);
                albumListFoldHolder.name = convertView.findViewById(R.id.tv_albumList_word_songList);
                albumListFoldHolder.setting = convertView.findViewById(R.id.iv_albumList_setting);
                albumListFoldHolder.name.setText(text[groupPosition - 1]);
                convertView.setTag(groupPosition == 1 ? R.id.album_list_group1 : R.id.album_list_group2,
                        albumListFoldHolder);
            }
        } else {
            if (groupPosition == 0) {
                albumListNormalHolder = (AlbumListNormalHolder) convertView.getTag(R.id.album_list_group0);
            } else {
                albumListFoldHolder = (AlbumListFoldHolder) convertView.getTag(groupPosition == 1 ? R.id.album_list_group1
                        : R.id.album_list_group2);
            }
        }
        if (groupPosition != 0){
            int drawableId = isExpanded?R.drawable.album_list_show_arrow:R.drawable.album_list_hide_arrow;
            albumListFoldHolder.arrow.setImageDrawable(context.getResources().getDrawable(drawableId));
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        AlbumListSongHolder albumListSongHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.album_list_rv_item_album, null);
            albumListSongHolder = new AlbumListSongHolder();
            albumListSongHolder.name = convertView.findViewById(R.id.tv_albumList_album_name);
            albumListSongHolder.num = convertView.findViewById(R.id.tv_albumList_album_num);
            albumListSongHolder.picture = convertView.findViewById(R.id.iv_albumList_album_picture);
            convertView.setTag(albumListSongHolder);
        } else {
            albumListSongHolder = (AlbumListSongHolder) convertView.getTag();
        }
        if (groupPosition == 1) {
            Glide.with(context)
                    .load(myList.get(childPosition).getCoverImgUrl())
                    .into(albumListSongHolder.picture);
            albumListSongHolder.num.setText("共" + myList.get(childPosition).getTrackCount() + "首");
            albumListSongHolder.name.setText(myList.get(childPosition).getName());
        } else {
            Glide.with(context)
                    .load(collectList.get(childPosition).getCoverImgUrl())
                    .into(albumListSongHolder.picture);
            albumListSongHolder.num.setText("共" + collectList.get(childPosition).getTrackCount() + "首");
            albumListSongHolder.name.setText(collectList.get(childPosition).getName());
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

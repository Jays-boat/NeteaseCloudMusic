package com.jayboat.music.ui.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import com.jayboat.music.App;
import com.jayboat.music.R;
import com.jayboat.music.adapter.AlbumListAdapter;
import com.jayboat.music.bean.AlbumList;
import com.jayboat.music.bean.PlaylistBean;
import com.jayboat.music.ui.activity.SongListActivity;
import com.jayboat.music.utils.NetUtils;
import com.jayboat.music.utils.ToastUtils;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumListFragment extends BaseFragment {

    public final static String TAG = "AlbumListFragment";

    private ExpandableListView mListView;

    private List<PlaylistBean> allList;

    @Override
    protected int getResId() {
        return R.layout.album_list_fragment;
    }

    @Override
    public void initView(@NonNull View view) {
        mListView = view.findViewById(R.id.elv_album_list);
        if (!App.existUserInfo()) {
            return;
        }
        NetUtils.getAlbumList(App.getUser().getAccount().getId(), new Callback<AlbumList>() {
            @Override
            public void onResponse(@NonNull Call<AlbumList> call, @NonNull Response<AlbumList> response) {
                if (response.body() != null) {
                    allList = response.body().getPlaylist();
                    loadData2View();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AlbumList> call, @NonNull Throwable t) {
                Log.v(TAG, "failed:(");
            }
        });
    }

    private void loadData2View(){
        ArrayList<PlaylistBean> myList = new ArrayList<>();
        ArrayList<PlaylistBean> collectList = new ArrayList<>();
        for (int i = 0; i < allList.size(); i++) {
            if (allList.get(i).getCreator().getUserId() == App.getUser().getAccount().getId()){
                myList.add(allList.get(i));
            } else {
                collectList.add(allList.get(i));
            }
        }
        AlbumListAdapter adapter = new AlbumListAdapter(getContext(),myList,collectList);
        mListView.setAdapter(adapter);
        mListView.setGroupIndicator(null);
        mListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            switch (groupPosition){
                case 0:
                    //todo: into local music list
                    break;
                case 1:
                    long musicId = myList.get(childPosition).getId();
                    showDetail(musicId);
                    break;
                case 2:
                    long musicId1 = collectList.get(childPosition).getId();
                    showDetail(musicId1);
                    break;
            }
            return true;
        });
    }


    private void showDetail(long id){
        Intent intent = new Intent(getActivity(), SongListActivity.class);
        intent.putExtra("SongListId",id);
        startActivity(intent);
    }
}
